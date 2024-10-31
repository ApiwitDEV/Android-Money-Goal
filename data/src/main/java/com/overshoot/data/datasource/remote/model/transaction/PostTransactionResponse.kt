package com.overshoot.data.datasource.remote.model.transaction

import com.squareup.moshi.Json

data class PostTransactionResponse(
    @field:Json(name = "data")
    val data: PostTransactionData?,
    @field:Json(name = "message")
    val message: String
)
