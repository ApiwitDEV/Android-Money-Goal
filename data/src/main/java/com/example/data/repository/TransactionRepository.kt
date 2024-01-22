package com.example.data.repository

import com.example.data.datasource.ResultData
import com.example.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun addTransaction(): ResultData<TransactionEntity>

    suspend fun subscribe(): Flow<TransactionEntity>
    suspend fun subscribe2(): Flow<TransactionEntity>

}