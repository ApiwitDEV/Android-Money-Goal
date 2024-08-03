package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.model.authentication.model.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun isSignedCheck(): Boolean

    fun loginWithEmail(email: String, password: String): Flow<ResultData<AuthResponse>>

    fun registerWithEmail(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<ResultData<AuthResponse>>

    fun logout()

    fun requestVerificationCode(): Flow<ResultData<Long>>

    fun verifyCode(verificationCode: String): Flow<ResultData<Int>>

}