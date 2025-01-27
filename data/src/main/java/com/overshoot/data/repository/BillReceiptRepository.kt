package com.overshoot.data.repository

import android.graphics.Bitmap
import com.overshoot.data.datasource.remote.model.receipt.ReceiptInfoResponse
import kotlinx.coroutines.flow.Flow

interface BillReceiptRepository {

    suspend fun submitBillReceipt(image: Bitmap, filename: String, type: String): Result<ReceiptInfoResponse>

    suspend fun resumableSubmitBillReceipt(image: Bitmap,filename: String, type: String): Flow<Result<ReceiptInfoResponse>>

}