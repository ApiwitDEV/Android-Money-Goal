package com.overshoot.moneygoal

import android.app.Application
import com.overshoot.data.dataModule
import com.overshoot.domain.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MoneyGoalApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val other = module {
            //single { MyFirebaseMessagingService(context = androidContext()) }
        }

        val allModule = listOf(appModule, domainModule, dataModule, other)

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