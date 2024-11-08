package com.overshoot.data.datasource.remote.model.transaction

import com.google.gson.annotations.SerializedName

data class PostTransactionRequestBody(
    val name: String,
    @SerializedName("category_id")
    val categoryId: String,
    val remark: String,
    val type: String,
    val value: Double
)
