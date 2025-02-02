package com.overshoot.domain

import com.overshoot.domain.usecase.goal.AddGoalUseCase
import com.overshoot.domain.usecase.transaction.AddTransactionUseCase
import com.overshoot.domain.usecase.goal.GetGoalUseCase
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCaseImpl
import com.overshoot.domain.usecase.authentication.LoginWithEmailUseCase
import com.overshoot.domain.usecase.authentication.LogoutUseCase
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RequestVerificationCodeUseCase
import com.overshoot.domain.usecase.authentication.VerifyCodeUseCase
import com.overshoot.domain.usecase.initial.LoadAllInitialDataUseCase
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    single<CoroutineScope> { CoroutineScope(Dispatchers.Default) }
    factory<SubscribeTransactionUseCase> { SubscribeTransactionUseCaseImpl(get(), get()) }
    factoryOf(::GetGoalUseCase)
    factoryOf(::AddGoalUseCase)
    factoryOf(::AddTransactionUseCase)
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::RegisterWithEmailUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::RequestVerificationCodeUseCase)
    factoryOf(::VerifyCodeUseCase)
    factoryOf(::LoadAllInitialDataUseCase)
}