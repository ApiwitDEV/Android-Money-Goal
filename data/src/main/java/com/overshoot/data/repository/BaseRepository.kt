package com.overshoot.data.repository

import com.overshoot.data.datasource.Failure
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import com.overshoot.data.datasource.remote.network.InternetConnectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

open class BaseRepository {

    private val internetConnectivity by inject<InternetConnectivity>(InternetConnectivity::class.java)

    suspend fun <T: Any> callDB(
        context: CoroutineContext = Dispatchers.IO,
        action: suspend CoroutineScope.() -> T
    ): ResultData<T> {
        return CoroutineScope(context = context)
            .async {
                try {
                    Success(data = action())
                } catch (e: Exception) {
                    Failure(message = e.message?:"")
                }
            }
            .await()
    }

    suspend fun <T: Any> callRestApi(
        action: suspend CoroutineScope.() -> Response<T>
    ): ResultData<T> {
        return if (internetConnectivity.isAvailable()) {
            try {
                withTimeout(5000) {
                    action().getResultData()
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

    private fun <T : Any> Response<T>.getResultData(): ResultData<T> {
        return try {
            when(this.code()) {
                401 -> Failure(message = "Unauthorized")
                500 -> Failure(message = "Server Error")
                else -> Success(this.body()!!)
            }
        } catch (e: Exception) {
            Failure(message = "unknown")
        }
    }

}