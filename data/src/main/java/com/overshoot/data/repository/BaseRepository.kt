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
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

open class BaseRepository {

    private val internetConnectivity by inject<InternetConnectivity>(InternetConnectivity::class.java)

    protected suspend fun <T: Any> callDB(
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

    protected suspend fun <T: Any> callRestApi(
        action: suspend CoroutineScope.() -> Response<T>
    ): ResultData<T> {
        return if (internetConnectivity.isAvailable()) {
            try {
                withTimeout(30000) {
                    action().getResultData()
                }
            } catch (e: Exception) {
                Failure(message = e.message?:"")
            }
        } else {
            Failure(message = "Connectivity Lost")
        }
    }

    protected suspend fun <T> callApi(call: suspend CoroutineScope.() -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = call()
                if (response.isSuccessful) {
                    response.body()?.let { Result.success(it) }?: Result.failure(exception = Exception("Response body is null"))
                } else {
                    Result.failure(exception = Exception(response.message()))
                }
            }
            catch (error: Exception) {
                when(error) {
                    is UnknownHostException -> Result.failure(exception = Exception("No Internet Connection OR Server Not Found "))
                    else -> Result.failure(exception = error)
                }
            }
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
        return when(this.code()) {
            401 -> Failure(message = "Unauthorized")
            500 -> Failure(message = "Server Error")
            else -> Success(this.body()!!)
        }
    }

}