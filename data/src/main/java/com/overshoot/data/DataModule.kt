package com.overshoot.data

import com.overshoot.data.datasource.local.GoalDatabase
import com.overshoot.data.datasource.local.hardware.SimCard
import com.overshoot.data.datasource.local.hardware.SimCardImpl
import com.overshoot.data.datasource.local.transaction.FakeTransactionDataSource
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.local.user.UserInfoDao
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
import com.overshoot.data.repository.TransactionRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<UserInfoDao> { object : UserInfoDao {
        override fun collectUserInfo() {
            println()
        }
    } }
    single { InternetConnectivity(androidContext()) }
    single<SimCard> { SimCardImpl() }
    single<AuthenticationService> { AuthenticationServiceImpl() }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            authenticationService = get(),
            userInfoDao = get(),
            moneyGoalApiService = RetrofitService.getMoneyGoalApiService(HttpClient.getClient()),
            simCard = get()
        )
    }
    single<StreamingDataSource<TransactionEntity>> {
        FakeTransactionDataSource()
    }
    single<GoalRepository> {
        GoalRepositoryImpl(
            goalDao = GoalDatabase.getDatabase(androidContext()).goalDao(),
            moneyGoalApiService = RetrofitService.getMoneyGoalApiService(HttpClient.getClient())
        )
    }
    single<TransactionRepository> {
        TransactionRepositoryImpl(get(), GoalDatabase.getDatabase(androidContext()).transactionDao())
    }
    single {
        CategoryRepository(GoalDatabase.getDatabase(androidContext()).categoryDao())
    }
}