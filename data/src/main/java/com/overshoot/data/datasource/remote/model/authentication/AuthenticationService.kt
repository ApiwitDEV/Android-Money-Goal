package com.overshoot.data.datasource.remote.model.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.overshoot.data.datasource.ResultData
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {

    suspend fun getAccessToken(): String?

    fun getUserInfo(): FirebaseUser?

    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (Task<AuthResult>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun registerWithEmail(
        email: String,
        password: String,
        onSuccess: (Task<AuthResult>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun logout()

    fun sendVerificationCode(phoneNumber: String, onSuccess: (Long) -> Unit, onFailure: (Exception) -> Unit)

    fun signInWithPhoneNumber(code: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

}