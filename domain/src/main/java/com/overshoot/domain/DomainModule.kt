package com.overshoot.domain

import org.koin.dsl.module

val domainModule = module {
    factory { GetGoalUseCase(get()) }
    factory { AddGoalUseCase(get()) }
    factory { AddTransactionUseCase(get()) }
}