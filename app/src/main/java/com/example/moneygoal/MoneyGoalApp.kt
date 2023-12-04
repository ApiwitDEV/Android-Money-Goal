package com.example.moneygoal

import android.app.Application
import com.example.data.repository.CurrentGoalRepository
import com.example.domain.GetCurrentGoalUseCase
import com.example.moneygoal.viewmodel.CurrentGoalViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MoneyGoalApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            viewModel { CurrentGoalViewModel(get()) }
        }

        val domainModule = module {
            factory { GetCurrentGoalUseCase(get()) }
        }

        val dataModule = module {
            single { CurrentGoalRepository() }
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

    }

}