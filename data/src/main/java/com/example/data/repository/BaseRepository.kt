package com.example.data.repository

import com.example.data.datasource.Failure
import com.example.data.datasource.ResultData
import com.example.data.datasource.Success
import com.example.data.datasource.remote.network.Connectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import org.koin.java.KoinJavaComponent.inject

open class BaseRepository {

    private val connectivity by inject<Connectivity>(Connectivity::class.java)

    suspend fun <T: Any> callDB(operation: suspend CoroutineScope.() -> T): ResultData<T> {
        return CoroutineScope(Dispatchers.Default)
            .async {
                try {
                    Success(data = operation())
                } catch (e: Exception) {
                    Failure(message = e.message?:"")
                }
            }
            .await()
    }

    suspend fun <T: Any> callRestFulApi(operation: suspend CoroutineScope.() -> T): ResultData<T> {
        return if (connectivity.isAvailable()) {
            try {
                withTimeout(5000) {
                    Success(data = operation())
                }
            } catch (e: Exception) {
                Failure(message = e.message?:"")
            }
        } else {
            Failure(message = "Connectivity Lost")
        }
    }

}