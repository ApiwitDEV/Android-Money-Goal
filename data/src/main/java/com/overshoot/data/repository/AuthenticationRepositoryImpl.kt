package com.overshoot.data.repository

import android.util.Log
import com.overshoot.data.datasource.Failure
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import com.overshoot.data.datasource.local.hardware.SimCard
import com.overshoot.data.datasource.local.user.UserInfoDao
import com.overshoot.data.datasource.local.user.UserInfoEntity
import com.overshoot.data.datasource.remote.MoneyGoalApiService
import com.overshoot.data.datasource.remote.model.authentication.AuthenticationService
import com.overshoot.data.datasource.remote.model.authentication.model.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService,
    private val userInfoDao: UserInfoDao,
    private val moneyGoalApiService: MoneyGoalApiService,
    private val simCard: SimCard
): BaseRepository(), AuthenticationRepository {

    override fun isSignedCheck(): Boolean {
        return authenticationService.getUserInfo() != null
    }

    override fun loginWithEmail(email: String, password: String): Flow<ResultData<AuthResponse>> {
        return callbackFlow {
            authenticationService.loginWithEmail(
                email = email,
                password = password,
                onSuccess = { taskAuth ->
                    taskAuth.result.user?.getIdToken(true)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val token = it.result.token
                                Log.i("token", token?:"")
                                launch(Dispatchers.IO) {
                                    launch {
                                        userInfoDao.saveUserInfo(
                                            UserInfoEntity(
                                                accessToken = token?:"",
                                                refreshToken = "",
                                                userName = "",
                                            )
                                        )
                                    }.join()
                                    Log.d("response", async { moneyGoalApiService.getInfo().toString() }.await())
                                    trySend(Success(AuthResponse(message = "Success")))
                                }
                            }
                            else {
                                trySend(Failure(message = it.exception?.message?:""))
                            }
                        }
                },
                onFailure = { exception ->
                    trySend(Failure(message = exception.message?:""))
                }
            )
            awaitClose {
                close()
            }
        }
    }

    override fun registerWithEmail(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<ResultData<AuthResponse>> {
        return callbackFlow {
            authenticationService.registerWithEmail(
                email,
                password,
                onSuccess = {

                },
                onFailure = { exception ->
                    trySend(Failure(message = exception.message?:""))
                }
            )
            awaitClose {
                close()
            }
        }
    }

    override fun logout() {
        authenticationService.logout()
    }

    override fun requestVerificationCode(): Flow<ResultData<Long>> {
        return callbackFlow {
            authenticationService.sendVerificationCode(
                phoneNumber = simCard.getPhoneNumber(),
                onSuccess = {
                    trySend(Success(it))
                },
                onFailure = {
                   trySend(Failure(message = it.message?:""))
                }
            )
            awaitClose {
                close()
            }
        }
    }

    override fun verifyCode(verificationCode: String): Flow<ResultData<Int>> {
        return callbackFlow {
            authenticationService.signInWithPhoneNumber(
                code = verificationCode,
                onSuccess = {
                    trySend(Success(-1))
                },
                onFailure = {
                    trySend(Failure(message = it.message?:""))
                }
            )
            awaitClose {
                close()
            }
        }
    }

}