package com.overshoot.moneygoalapp

import com.overshoot.authentication.stateholder.SignInViewModel
import com.overshoot.authentication.stateholder.SignUpViewModel
import com.overshoot.moneygoalapp.component.account.stateholder.AccountViewModel
import com.overshoot.moneygoalapp.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoalapp.component.home.stateholder.viewmodel.HomeTransactionViewModel
import com.overshoot.moneygoalapp.component.notification.NotificationViewModel
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillViewModel
import com.overshoot.moneygoalapp.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import com.overshoot.moneygoalapp.flutterinteractor.FlutterInterface
import com.overshoot.moneygoalapp.flutterinteractor.FlutterMainExecutor
import com.overshoot.moneygoalapp.flutterinteractor.FlutterTestExecutor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
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
    viewModelOf(::SignInViewModel)
    viewModel { NotificationViewModel() }
    viewModel { HomeGoalDetailViewModel(get(), get()) }
    viewModelOf(::HomeTransactionViewModel)
    viewModelOf(::TransactionHistoryViewModel)
    viewModel { ScanBillViewModel(get()) }
    viewModelOf(::AccountViewModel)
}