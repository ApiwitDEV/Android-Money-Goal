package com.overshoot.domain

import com.overshoot.domain.usecase.AddGoalUseCase
import com.overshoot.domain.usecase.AddTransactionUseCase
import com.overshoot.domain.usecase.GetGoalUseCase
import com.overshoot.domain.usecase.SubscribeTransactionUseCase
import com.overshoot.domain.usecase.authentication.LoginWithEmailUseCase
import com.overshoot.domain.usecase.authentication.LogoutUseCase
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RequestVerificationCodeUseCase
import com.overshoot.domain.usecase.authentication.VerifyCodeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    factory { GetGoalUseCase(get()) }
    factory { AddGoalUseCase(get()) }
    factory { AddTransactionUseCase(get()) }
    factory { SubscribeTransactionUseCase(get(), get()) }
    factory { LoginWithEmailUseCase(get()) }
    factory { RegisterWithEmailUseCase(get()) }
    factory { LogoutUseCase(CoroutineScope(Dispatchers.IO), get()) }
    factory { RequestVerificationCodeUseCase(get()) }
    factory { VerifyCodeUseCase(get()) }
}