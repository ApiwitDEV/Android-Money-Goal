package com.overshoot.healthgoalapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.overshoot.data.dataModule
import com.overshoot.domain.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HealthGoalApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this@HealthGoalApp)
        val allModule = listOf(appModule, domainModule, dataModule)

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@HealthGoalApp)
            // Load modules
            modules(allModule)
        }
    }

}