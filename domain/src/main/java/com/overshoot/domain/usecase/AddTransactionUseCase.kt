package com.overshoot.domain.usecase

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class AddTransactionUseCase(private val transactionRepository: TransactionRepository) {

    suspend fun addTransaction(
        name: String,
        categoryId: Int,
        remark: String,
        type: String,
        value: Double
    ): ResultData<Unit> {
        return transactionRepository.addTransaction(
            name = name,
            categoryId = categoryId,
            remark = remark,
            type = type,
            value = value
        )
    }

    suspend fun subscribe(): Flow<TransactionEntity> {
        return transactionRepository.subscribe()
    }

    suspend fun subscribe2(): Flow<List<TransactionEntity>> {
        return transactionRepository.subscribe2()
    }

}