package com.overshoot.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.overshoot.data.datasource.remote.model.receipt.ReceiptInfoResponse
import kotlinx.coroutines.flow.Flow

interface BillReceiptRepository {

    suspend fun submitBillReceipt(image: Bitmap, filename: String, type: String): Result<ReceiptInfoResponse>

    suspend fun chunkSubmitBillReceipt(image: Uri, filename: String, type: String): Flow<Result<ReceiptInfoResponse>>

    suspend fun cancelChunkUploadBillReceipt(): Result<Unit>?

    suspend fun deleteUploadedFile(filename: String): Result<Unit>?

}