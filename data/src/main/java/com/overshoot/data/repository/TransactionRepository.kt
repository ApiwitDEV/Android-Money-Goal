package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun addTransaction(): ResultData<TransactionEntity>

    suspend fun subscribe(): Flow<TransactionEntity>
    suspend fun subscribe2(): Flow<TransactionEntity>

}