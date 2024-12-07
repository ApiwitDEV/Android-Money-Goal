package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(
        name: String,
        categoryId: String,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse>

    suspend fun getAllTransaction()

    fun subscribe(): Flow<List<TransactionEntity>>

    suspend fun deleteAllTransactions()

    suspend fun deleteTransactions(transactionIds: List<String>): ResultData<String>
}