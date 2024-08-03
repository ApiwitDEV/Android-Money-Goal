package com.overshoot.domain.usecase.authentication

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.model.authentication.model.AuthResponse
import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class LoginWithEmailUseCase(private val authenticationRepository: AuthenticationRepository) {

    operator fun invoke(email: String, password: String): Flow<ResultData<AuthResponse>> {
        return authenticationRepository.loginWithEmail(email, password)
    }

}