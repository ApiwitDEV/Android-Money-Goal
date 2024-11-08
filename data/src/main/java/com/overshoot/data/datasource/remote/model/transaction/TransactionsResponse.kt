package com.overshoot.data.datasource.remote.model.transaction

import com.squareup.moshi.Json

data class GetTransactionsResponse(
    @field:Json(name = "transactions")
    val transactions: List<TransactionData>,
    @field:Json(name = "message")
    val message: String
)
