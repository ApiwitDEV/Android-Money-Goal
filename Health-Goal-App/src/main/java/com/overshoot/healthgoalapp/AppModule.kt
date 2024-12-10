package com.overshoot.healthgoalapp

import com.overshoot.authentication.stateholder.SignInViewModel
import com.overshoot.authentication.stateholder.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
//    single<FlutterInterface>(createdAtStart = true, qualifier = qualifier("main")) {
//        FlutterMainExecutor(
//            context = androidApplication(),
//            flutterEngineId = "main_engine"
//        )
//    }
//    single<FlutterInterface>(createdAtStart = true, qualifier = qualifier("test")) {
//        FlutterTestExecutor(
//            context = androidApplication(),
//            flutterEngineId = "test_engine"
//        )
//    }
    viewModel { SignUpViewModel(get()) }
    viewModelOf(::SignInViewModel)
//    viewModel { NotificationViewModel() }
//    viewModel { HomeGoalDetailViewModel(get(), get()) }
//    viewModelOf(::HomeTransactionViewModel)
//    viewModelOf(::TransactionHistoryViewModel)
//    viewModel { ScanViewModel() }
//    viewModelOf(::AccountViewModel)
}