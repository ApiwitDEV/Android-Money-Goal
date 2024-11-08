package com.overshoot.data.datasource.remote.model.transaction

import com.google.gson.annotations.SerializedName

data class GetTransactionsResponse(
    @SerializedName("transactions")
    val transactions: List<TransactionData>,
    @SerializedName("message")
    val message: String
)
