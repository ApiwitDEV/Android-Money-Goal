package com.overshoot.domain.usecase.authentication

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.model.authentication.model.AuthResponse
import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class RegisterWithEmailUseCase(private val authenticationRepository: AuthenticationRepository) {

    operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<ResultData<AuthResponse>> {
        return authenticationRepository.registerWithEmail(
            firstName,
            lastName,
            email,
            password
        )
    }

}