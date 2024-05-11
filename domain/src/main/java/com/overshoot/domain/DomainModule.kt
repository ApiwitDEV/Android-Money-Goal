package com.overshoot.domain

import com.overshoot.domain.usecase.AddGoalUseCase
import com.overshoot.domain.usecase.AddTransactionUseCase
import com.overshoot.domain.usecase.GetGoalUseCase
import com.overshoot.domain.usecase.SubscribeTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetGoalUseCase(get()) }
    factory { AddGoalUseCase(get()) }
    factory { AddTransactionUseCase(get()) }
    factory { SubscribeTransactionUseCase(get(), get()) }
}