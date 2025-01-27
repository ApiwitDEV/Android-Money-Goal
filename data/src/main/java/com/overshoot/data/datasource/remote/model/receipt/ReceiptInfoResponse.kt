package com.overshoot.data.datasource.remote.model.receipt

import com.google.gson.annotations.SerializedName

data class ReceiptInfoResponse(
    @SerializedName("content_type")
    val contentType: String?,
    @SerializedName("detail")
    val detail: Detail?,
    @SerializedName("file_name")
    val fileName: String?,
    @SerializedName("file_size")
    val fileSize: Long?,
    @SerializedName("message")
    val message: String?
)
