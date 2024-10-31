package com.overshoot.domain.usecase.transaction

import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.TransactionRepository
import com.overshoot.domain.model.TransactionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SubscribeTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {

    operator fun invoke(): Flow<List<TransactionResult>> {
        return combine(
            flow = transactionRepository.subscribe(),
            flow2 = categoryRepository.subscribeCategory(),
            transform = { transactionList, categoryList ->
                transactionList.map { transaction ->
                    TransactionResult(
                        id = transaction.id,
                        name = transaction.name,
                        type = transaction.type,
                        value = transaction.moneyAmount,
                        remark = transaction.remark,
                        goalId = 0,
                        categoryId = transaction.categoryId?:0,
                        categoryName = categoryList.find { category ->
                            category.id == transaction.categoryId
                        }?.name.toString()
                    )
                }
            }
        )
    }

}