package com.overshoot.domain.usecase.transaction

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import com.overshoot.data.repository.TransactionRepository

class AddTransactionUseCase(private val transactionRepository: TransactionRepository) {

    suspend operator fun invoke(
        name: String,
        categoryId: String,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse> {
        return transactionRepository.addTransaction(
            name = name,
            categoryId = categoryId,
            remark = remark,
            type = type,
            value = value
        )
    }
}