package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.authentication.model.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun loginWithEmail(email: String, password: String): Flow<ResultData<AuthResponse>>

    fun registerWithEmail(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<ResultData<AuthResponse>>

    fun logout()

}