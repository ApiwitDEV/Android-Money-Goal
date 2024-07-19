package com.overshoot.domain.usecase.authentication

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class VerifyCodeUseCase(private val authenticationRepository: AuthenticationRepository) {

    operator fun invoke(code: String): Flow<ResultData<Int>> {
        return authenticationRepository.verifyCode(code)
    }

}