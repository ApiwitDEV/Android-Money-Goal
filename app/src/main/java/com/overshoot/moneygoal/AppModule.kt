package com.overshoot.moneygoal

import com.overshoot.moneygoal.component.authentication.stateholder.SignInSignUpViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoal.component.notification.NotificationViewModel
import com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SignInSignUpViewModel(get(), get()) }
    viewModel { NotificationViewModel() }
    viewModel { HomeGoalDetailViewModel(get(), get(), get()) }
    viewModel { HomeTransactionViewModel(get(), get()) }
    viewModel { TransactionHistoryViewModel(get()) }
}