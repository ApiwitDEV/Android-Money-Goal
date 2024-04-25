package com.overshoot.moneygoal

import com.overshoot.moneygoal.component.home.stateholder.viewmodel.GoalViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.TransactionViewModel
import com.overshoot.moneygoal.component.notification.NotificationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { NotificationViewModel() }
    viewModel { GoalViewModel(get(), get()) }
    viewModel { TransactionViewModel(get()) }
}