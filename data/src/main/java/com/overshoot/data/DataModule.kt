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
import com.overshoot.data.datasource.remote.network.getMoneyGoalApiService
import com.overshoot.data.repository.AuthenticationRepository
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.CategoryRepositoryImpl
import com.overshoot.data.repository.GoalRepository
import com.overshoot.data.repository.TransactionRepository
import com.overshoot.data.repository.TransactionRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module


val dataModule = module {
    singleOf<AuthenticationService>(::AuthenticationServiceImpl)
    single { UserDatabase.getDatabase(androidContext()).getUserInfoDao() }
    single { GoalDatabase.getDatabase(androidContext()).goalDao() }
    single { GoalDatabase.getDatabase(androidContext()).transactionDao() }
    single { GoalDatabase.getDatabase(androidContext()).temporaryTransactionDao() }
    single { GoalDatabase.getDatabase(androidContext()).categoryDao() }
    single { InternetConnectivity(androidContext()) }
    single {
        getMoneyGoalApiService(
            HttpClient.getClient(
                UserDatabase.getDatabase(androidContext()).getUserInfoDao(), get()
            )
        )
    }
    singleOf<SimCard>(::SimCardImpl)
    singleOf(::AuthenticationRepository)
    singleOf(::GoalRepository)
    singleOf<StreamingDataSource<TransactionEntity>> (::FakeTransactionDataSource)
    single<CategoryRepository> { CategoryRepositoryImpl(get(),get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get(),get(),get()) }
}