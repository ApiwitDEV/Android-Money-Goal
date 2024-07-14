package com.overshoot.domain.usecase.authentication

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.authentication.model.AuthResponse
import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class LoginWithEmailUseCase(
    private val coroutineScope: CoroutineScope,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<ResultData<AuthResponse>> {
        return coroutineScope.async {
            authenticationRepository.loginWithEmail(email, password)
        }.await()
    }

}