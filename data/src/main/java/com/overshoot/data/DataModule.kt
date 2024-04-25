package com.overshoot.data

import com.overshoot.data.datasource.local.GoalDatabase
import com.overshoot.data.datasource.local.transaction.FakeTransactionDataSource
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.remote.network.Connectivity
import com.overshoot.data.repository.GoalRepository
import com.overshoot.data.repository.GoalRepositoryImpl
import com.overshoot.data.repository.TransactionRepository
import com.overshoot.data.repository.TransactionRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Connectivity(androidContext())
    }
    single<StreamingDataSource<TransactionEntity>> {
        FakeTransactionDataSource()
    }
    single<GoalRepository> {
        GoalRepositoryImpl(GoalDatabase.getDatabase(androidContext()).goalDao())
    }
    single<TransactionRepository> {
        TransactionRepositoryImpl(get())
    }
}