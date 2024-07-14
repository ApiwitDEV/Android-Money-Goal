package com.overshoot.domain.usecase.authentication

import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LogoutUseCase(
    private val coroutineScope: CoroutineScope,
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke() {
        coroutineScope.launch {
            authenticationRepository.logout()
        }
    }

}