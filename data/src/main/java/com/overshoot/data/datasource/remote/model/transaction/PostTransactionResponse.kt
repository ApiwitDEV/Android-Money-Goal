package com.overshoot.data.datasource.remote.model.transaction

import com.google.gson.annotations.SerializedName

data class PostTransactionResponse(
    val transaction: TransactionData?,
    @SerializedName("message")
    val message: String
)
