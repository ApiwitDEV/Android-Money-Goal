package com.overshoot.data.datasource.remote.model.transaction

data class PostTransactionResponse(
    val transaction: TransactionData?,
    val message: String
)
