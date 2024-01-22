package com.example.data.repository

import com.example.data.datasource.Failure
import com.example.data.datasource.ResultData
import com.example.data.datasource.Success
import com.example.data.datasource.remote.network.Connectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.CoroutineContext

open class BaseRepository {

    private val connectivity by inject<Connectivity>(Connectivity::class.java)

    suspend fun <T: Any> callDB(
        context: CoroutineContext = Dispatchers.Default,
        operation: suspend CoroutineScope.() -> T
    ): ResultData<T> {
        return CoroutineScope(context = context)
            .async {
                try {
                    Success(data = operation())
                } catch (e: Exception) {
                    Failure(message = e.message?:"")
                }
            }
            .await()
    }

    suspend fun <T: Any> callRestFulApi(
        operation: suspend CoroutineScope.() -> T
    ): ResultData<T> {
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

    suspend fun <T: Any> subscribeStreamingData(subscribe: suspend ( suspend (T) -> Unit ) -> Unit): Flow<T> {
        return flow {
            subscribe {
                emit(it)
            }
        }
    }

}