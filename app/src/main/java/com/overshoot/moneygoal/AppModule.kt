package com.overshoot.moneygoal

import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoal.component.notification.NotificationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { NotificationViewModel() }
    viewModel { HomeGoalDetailViewModel(get(), get()) }
    viewModel { HomeTransactionViewModel(get()) }
}