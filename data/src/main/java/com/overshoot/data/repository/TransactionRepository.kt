package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun addTransaction(
        name: String,
        categoryId: Int,
        remark: String,
        type: String,
        value: Double
    ): ResultData<Unit>

    suspend fun subscribe(): Flow<TransactionEntity>
    fun subscribe2(): Flow<List<TransactionEntity>>

}