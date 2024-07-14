package com.overshoot.domain.usecase.authentication

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.authentication.model.AuthResponse
import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class RegisterWithEmailUseCase(
    private val coroutineScope: CoroutineScope,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<ResultData<AuthResponse>> {
        return coroutineScope.async {
            authenticationRepository.registerWithEmail(
                firstName,
                lastName,
                email,
                password
            )
        }.await()
    }

}