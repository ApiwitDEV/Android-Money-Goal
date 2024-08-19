package com.overshoot.moneygoal

import com.example.authentication.stateholder.SignInViewModel
import com.example.authentication.stateholder.SignUpViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoal.component.notification.NotificationViewModel
import com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import com.overshoot.moneygoal.flutterinteractor.FlutterInterface
import com.overshoot.moneygoal.flutterinteractor.FlutterMainExecutor
import com.overshoot.moneygoal.flutterinteractor.FlutterTestExecutor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val appModule = module {
    single<FlutterInterface>(createdAtStart = true, qualifier = qualifier("main")) {
        FlutterMainExecutor(
            context = androidApplication(),
            flutterEngineId = "main_engine"
        )
    }
    single<FlutterInterface>(createdAtStart = true, qualifier = qualifier("test")) {
        FlutterTestExecutor(
            context = androidApplication(),
            flutterEngineId = "test_engine"
        )
    }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get(), get(), get(), get(), get()) }
    viewModel { NotificationViewModel() }
    viewModel { HomeGoalDetailViewModel(get(), get(), get()) }
    viewModel { HomeTransactionViewModel(get(), get()) }
    viewModel { TransactionHistoryViewModel(get()) }
}