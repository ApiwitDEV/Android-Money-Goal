package com.overshoot.data.datasource.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URL
import java.util.concurrent.TimeUnit

class UploadFileService {

    data class UploadFileResult(
        val progress: Double,
        val isComplete: Boolean
    )

    fun uploadFile(uploadSessionUrl: URL, file: ByteArray, type: String): Flow<Result<UploadFileResult>> {
        val httpLogger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLogger)
            .build()

        return flow {
            val fileSize = file.size
            val maxChunkSize = 1024*512
            val chunks = file.toList().chunked(maxChunkSize) { it.toByteArray() }
            var byteSum = 0
            for (index in chunks.indices) {
                val byteChunk = chunks[index]
                val body = byteChunk.toRequestBody()
                val request = Request.Builder()
                    .url(uploadSessionUrl)
                    .header("Content-Type", type)
                    .header("Content-Range", "bytes ${byteSum}-${byteSum + byteChunk.size - 1}/$fileSize")
                    .put(body)
                    .build()
                try {
                    val response = client.newCall(request).execute()
                    byteSum += byteChunk.size
                    if (response.code == 308 || response.isSuccessful) {
                        val progress = (index.toDouble()*maxChunkSize) / fileSize
                        if (index == chunks.lastIndex) {
                            emit(
                                Result.success(
                                    UploadFileResult(
                                        progress = progress,
                                        isComplete = true
                                    )
                                )
                            )
                        }
                        else {
                            emit(
                                Result.success(
                                    UploadFileResult(
                                        progress = progress,
                                        isComplete = false
                                    )
                                )
                            )
                        }
                    }
                    else {
                        // fail from server
                        emit(Result.failure(Exception(response.message)))
                        break
                    }
                }
                catch (error: Exception) {
                    // fail from other
                    throw error
                }
            }
        }
    }

}