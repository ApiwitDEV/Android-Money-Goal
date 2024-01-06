package com.example.moneygoal

import android.app.Application
import com.example.data.datasource.remote.network.Connectivity
import com.example.data.repository.GoalRepository
import com.example.data.repository.GoalRepositoryImpl
import com.example.domain.GetGoalUseCase
import com.example.domain.AddGoalUseCase
import com.example.moneygoal.viewmodel.GoalViewModel
import com.example.moneygoal.viewmodel.TransactionViewModel
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MoneyGoalApp: Application() {

    private val connectivity by inject<Connectivity>()

    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            viewModel { GoalViewModel(get(), get()) }
            viewModel { TransactionViewModel() }
        }

        val domainModule = module {
            factory { GetGoalUseCase(get()) }
            factory { AddGoalUseCase(get()) }
        }

        val dataModule = module {
            single { Connectivity(androidContext()) }
            single<GoalRepository> { GoalRepositoryImpl() }
        }

        val allModule = listOf(appModule, domainModule, dataModule)

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MoneyGoalApp)
            // Load modules
            modules(allModule)
        }

        connectivity.requestNetwork()

    }

}