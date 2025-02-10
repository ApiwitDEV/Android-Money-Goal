package com.overshoot.data.datasource.remote

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

class UploadFileService(private val context: Context) {

    private var uploadSessionUrl: URL? = null

    data class UploadFileResult(
        val isComplete: Boolean,
        val progress: Double = 0.0,
        val message: String = "",
        val uploadedBytes: Long = 0L
    )

    private val httpLogger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLogger)
        .build()

    fun uploadFile(uploadSessionUrl: URL, fileUri: Uri, type: String): Flow<Result<UploadFileResult>> {
        this.uploadSessionUrl = uploadSessionUrl
        val maxChunkSize: Int = 256 * 1024
        return flow {
            val contentResolver: ContentResolver = context.contentResolver
            val contentLength = contentResolver.openFileDescriptor(fileUri, "r").use {
                it?.statSize?: -1L
            }
            if (contentLength == -1L) {
                emit(Result.failure(IOException("Could not determine file size.")))
                return@flow
            }
            contentResolver.openInputStream(fileUri).use { inputStream ->
                inputStream?.also {
                    val buffer = ByteArray(maxChunkSize)
                    var uploadedBytes = 0L
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {

                        val requestBody = buffer.copyOfRange(0, bytesRead).toRequestBody()
                        val request = okhttp3.Request.Builder()
                            .url(uploadSessionUrl)
                            .header("Content-Type", type)
                            .header("Content-Range", "bytes ${uploadedBytes}-${uploadedBytes + bytesRead - 1}/$contentLength")
                            .put(requestBody)
                            .build()

                        try {
                            val response = client.newCall(request).execute()
                            if (response.code == 308) {
                                uploadedBytes += bytesRead
                                val progress = (uploadedBytes * 100.0 / contentLength)
                                emit(Result.success(UploadFileResult(false, progress = progress, uploadedBytes = uploadedBytes, message = "Uploading...")))
                            } else if (response.isSuccessful) {
                                emit(Result.success(UploadFileResult(true, 100.0, uploadedBytes = uploadedBytes, message = "Upload completed successfully.")))
                            } else {
                                emit(Result.failure(IOException("Upload failed: ${response.code} ${response.message}")))
                                return@flow
                            }
                        } catch (e: Exception) {
                            emit(Result.failure(IOException("Upload failed: ${e.message}", e)))
                        }

                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun cancelUploadSession(): Result<Unit>? {
        return this.uploadSessionUrl?.let {
            val request = okhttp3.Request.Builder()
                .url(it)
                .delete()
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(Unit)
            }
            else {
                Result.failure(exception = Exception(""))
            }
        }
    }

}