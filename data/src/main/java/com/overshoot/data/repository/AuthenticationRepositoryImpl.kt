package com.overshoot.data.repository

import com.overshoot.data.datasource.Failure
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import com.overshoot.data.datasource.local.hardware.SimCard
import com.overshoot.data.datasource.local.user.UserInfoDao
import com.overshoot.data.datasource.remote.RestfulApiService
import com.overshoot.data.datasource.remote.authentication.AuthenticationService
import com.overshoot.data.datasource.remote.authentication.model.AuthResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService,
    private val userInfoDao: UserInfoDao,
    private val restfulApiService: RestfulApiService,
    private val simCard: SimCard
): BaseRepository(), AuthenticationRepository {

    override fun loginWithEmail(email: String, password: String): Flow<ResultData<AuthResponse>> {
        return callbackFlow {
            authenticationService.loginWithEmail(
                email = email,
                password = password,
                onSuccess = {
                    trySend(Success(AuthResponse(message = "Success")))
                    userInfoDao.collectUserInfo()
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
                    restfulApiService.collectUserInfo()
                    userInfoDao.collectUserInfo()
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