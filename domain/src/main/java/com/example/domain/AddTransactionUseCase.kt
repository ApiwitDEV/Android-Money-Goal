package com.example.domain

import com.example.data.datasource.ResultData
import com.example.data.datasource.local.transaction.TransactionEntity
import com.example.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class AddTransactionUseCase(private val transactionRepository: TransactionRepository) {

    suspend fun addTransaction(): ResultData<TransactionEntity> {
        return transactionRepository.addTransaction()
    }

    suspend fun subscribe(): Flow<TransactionEntity> {
        return transactionRepository.subscribe()
    }

    suspend fun subscribe2(): Flow<TransactionEntity> {
        return transactionRepository.subscribe2()
    }

}