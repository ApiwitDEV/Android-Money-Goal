package com.overshoot.data.datasource.remote.model.transaction

data class GetTransactionsResponse(
    val transactions: List<TransactionData>,
    val message: String
)
