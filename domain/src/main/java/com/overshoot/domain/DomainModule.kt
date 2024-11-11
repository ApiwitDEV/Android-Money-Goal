package com.overshoot.domain

import com.overshoot.domain.usecase.goal.AddGoalUseCase
import com.overshoot.domain.usecase.transaction.AddTransactionUseCase
import com.overshoot.domain.usecase.goal.GetGoalUseCase
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCase
import com.overshoot.domain.usecase.authentication.LoginWithEmailUseCase
import com.overshoot.domain.usecase.authentication.LogoutUseCase
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RequestVerificationCodeUseCase
import com.overshoot.domain.usecase.authentication.VerifyCodeUseCase
import com.overshoot.domain.usecase.initial.LoadAllInitialDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get

val domainModule = module {
    single<CoroutineScope> { CoroutineScope(Dispatchers.Default) }
    factory { GetGoalUseCase(get()) }
    factory { AddGoalUseCase(get()) }
    factory { AddTransactionUseCase(get()) }
    factory { SubscribeTransactionUseCase(get(), get()) }
    factory { LoginWithEmailUseCase(get()) }
    factory { RegisterWithEmailUseCase(get()) }
    factoryOf(::LogoutUseCase)
    factory { RequestVerificationCodeUseCase(get()) }
    factory { VerifyCodeUseCase(get()) }
    factoryOf(::LoadAllInitialDataUseCase)
}