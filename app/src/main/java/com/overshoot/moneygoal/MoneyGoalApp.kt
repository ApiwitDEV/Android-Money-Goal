package com.overshoot.moneygoal

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.overshoot.data.datasource.local.transaction.FakeTransactionDataSource
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.remote.network.Connectivity
import com.overshoot.data.repository.GoalRepository
import com.overshoot.data.repository.GoalRepositoryImpl
import com.overshoot.data.repository.TransactionRepository
import com.overshoot.data.repository.TransactionRepositoryImpl
import com.overshoot.domain.GetGoalUseCase
import com.overshoot.domain.AddGoalUseCase
import com.overshoot.domain.AddTransactionUseCase
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.GoalViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.TransactionViewModel
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
            viewModel { NotificationViewModel() }
            viewModel { GoalViewModel(get(), get()) }
            viewModel { TransactionViewModel(get()) }
        }

        val domainModule = module {
            factory { GetGoalUseCase(get()) }
            factory { AddGoalUseCase(get()) }
            factory { AddTransactionUseCase(get()) }
        }

        val dataModule = module {

            single<StreamingDataSource<TransactionEntity>> { FakeTransactionDataSource() }

            single { Connectivity(androidContext()) }
            single<GoalRepository> { GoalRepositoryImpl() }
            single<TransactionRepository> { TransactionRepositoryImpl(get()) }
        }

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

        connectivity.requestNetwork()
    }
}