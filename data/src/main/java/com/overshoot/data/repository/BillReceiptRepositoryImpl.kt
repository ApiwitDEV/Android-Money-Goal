package com.overshoot.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.overshoot.data.datasource.remote.MoneyGoalApiService
import com.overshoot.data.datasource.remote.UploadFileService
import com.overshoot.data.datasource.remote.model.receipt.ReceiptInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class BillReceiptRepositoryImpl(
    private val moneyGoalApiService: MoneyGoalApiService,
    private val uploadFileService: UploadFileService
): BaseRepository(), BillReceiptRepository {

    override suspend fun submitBillReceipt(
        image: Bitmap,
        filename: String,
        type: String
    ): Result<ReceiptInfoResponse> {
//        context.contentResolver.openInputStream(image).use {
//            val requestFile =
//                it?.readBytes()?.toRequestBody(type.toMediaTypeOrNull(), 0)
//            val bodyPart = MultipartBody.Part.createFormData(name = "uploaded_file", filename = filename, body = requestFile?: byteArrayOf().toRequestBody())
//        }
        val requestFile =
            image.toByteArray().toRequestBody(type.toMediaTypeOrNull(), 0)
        val bodyPart = MultipartBody.Part.createFormData(name = "uploaded_file", filename = filename, body = requestFile)
        return callApi { moneyGoalApiService.extractInfoFromImage(bodyPart) }
    }

    override suspend fun chunkSubmitBillReceipt(
        image: Uri,
        filename: String,
        type: String
    ): Flow<Result<ReceiptInfoResponse>> {
        return callbackFlow {
            send(
                Result.success(
                    ReceiptInfoResponse(
                        contentType = null,
                        detail = null,
                        fileName = null,
                        fileSize = null,
                        errorMessage = null,
                        status = "preparing",
                        progress = 0.0
                    )
                )
            )
            val sessionResult = callApi { moneyGoalApiService.getUploadSession(filename) }
            if (sessionResult.isSuccess) {
                send(
                    Result.success(
                        ReceiptInfoResponse(
                            contentType = null,
                            detail = null,
                            fileName = null,
                            fileSize = null,
                            errorMessage = null,
                            status = "incomplete",
                            progress = 0.0
                        )
                    )
                )
                withContext(Dispatchers.IO) {
                    sessionResult.getOrNull()?.let { uploadSessionUrl ->
                        uploadFileService.uploadFile(uploadSessionUrl, image, type)
                            .catch { error ->
                                // fail from other
                                emit(Result.failure(error))
                            }
                            .collect { result ->
                                result
                                    .onSuccess {
                                        if (it.isComplete) {
                                            send(
                                                Result.success(
                                                    ReceiptInfoResponse(
                                                        contentType = null,
                                                        detail = null,
                                                        fileName = null,
                                                        fileSize = null,
                                                        errorMessage = null,
                                                        status = "incomplete",
                                                        progress = it.progress
                                                    )
                                                )
                                            )
                                            delay(1000)
                                            send(
                                                Result.success(
                                                    ReceiptInfoResponse(
                                                        contentType = null,
                                                        detail = null,
                                                        fileName = null,
                                                        fileSize = null,
                                                        errorMessage = null,
                                                        status = "upload complete",
                                                        progress = it.progress
                                                    )
                                                )
                                            )
                                            val receiptInfoResult =
                                                callApi { moneyGoalApiService.extractInfoFromImage2(filename = filename, type = type) }
                                            send(receiptInfoResult)
                                            close()
                                        }
                                        else {
                                            send(
                                                Result.success(
                                                    ReceiptInfoResponse(
                                                        contentType = null,
                                                        detail = null,
                                                        fileName = null,
                                                        fileSize = null,
                                                        errorMessage = null,
                                                        status = "incomplete",
                                                        progress = it.progress
                                                    )
                                                )
                                            )
                                        }
                                    }
                                    .onFailure { error ->
                                        // fail from server
                                        send(Result.failure(error))
                                    }
                            }
                    }
                }
            }
            else {
                sessionResult.exceptionOrNull()?.also {
                    send(Result.failure(it))
                }
            }
            awaitClose()
        }
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

}