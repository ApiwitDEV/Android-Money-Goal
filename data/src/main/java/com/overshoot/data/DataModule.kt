package com.overshoot.data

import com.overshoot.data.datasource.local.GoalDatabase
import com.overshoot.data.datasource.local.UserDatabase
import com.overshoot.data.datasource.local.hardware.SimCard
import com.overshoot.data.datasource.local.hardware.SimCardImpl
import com.overshoot.data.datasource.local.transaction.FakeTransactionDataSource
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.remote.model.authentication.AuthenticationService
import com.overshoot.data.datasource.remote.model.authentication.AuthenticationServiceImpl
import com.overshoot.data.datasource.remote.network.HttpClient
import com.overshoot.data.datasource.remote.network.InternetConnectivity
import com.overshoot.data.datasource.remote.network.RetrofitService
import com.overshoot.data.repository.AuthenticationRepository
import com.overshoot.data.repository.AuthenticationRepositoryImpl
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.GoalRepository
import com.overshoot.data.repository.GoalRepositoryImpl
import com.overshoot.data.repository.TransactionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val dataModule = module {
    single { InternetConnectivity(androidContext()) }
    single<SimCard> { SimCardImpl() }
    single<AuthenticationService> { AuthenticationServiceImpl() }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            authenticationService = get(),
            userInfoDao = UserDatabase.getDatabase(androidContext()).getUserInfoDao(),
            moneyGoalApiService = RetrofitService.getMoneyGoalApiService(HttpClient.getClient(UserDatabase.getDatabase(androidContext()).getUserInfoDao(), get())),
            simCard = get()
        )
    }
    single<StreamingDataSource<TransactionEntity>> {
        FakeTransactionDataSource()
    }
    single<GoalRepository> {
        GoalRepositoryImpl(
            goalDao = GoalDatabase.getDatabase(androidContext()).goalDao(),
            moneyGoalApiService = RetrofitService.getMoneyGoalApiService(HttpClient.getClient(UserDatabase.getDatabase(androidContext()).getUserInfoDao(), get()))
        )
    }
    single {
        TransactionRepository(
            get(),
            GoalDatabase.getDatabase(androidContext()).transactionDao(),
            RetrofitService.getMoneyGoalApiService(HttpClient.getClient(UserDatabase.getDatabase(androidContext()).getUserInfoDao(), get()))
        )
    }
    single {
        CategoryRepository(GoalDatabase.getDatabase(androidContext()).categoryDao())
    }
}