package com.overshoot.domain.usecase.authentication

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class RequestVerificationCodeUseCase(private val authenticationRepository: AuthenticationRepository) {

    operator fun invoke(): Flow<ResultData<Long>> {
        return authenticationRepository.requestVerificationCode()
    }

}