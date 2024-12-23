package com.overshoot.domain.usecase.transaction

import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.TransactionRepository
import com.overshoot.domain.model.TransactionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SubscribeTransactionUseCaseImpl(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
): SubscribeTransactionUseCase {

    override operator fun invoke(): Flow<List<TransactionResult>> {
        return combine(
            flow = transactionRepository.subscribe(),
            flow2 = categoryRepository.subscribeCategory(),
            transform = { transactionList, categoryList ->
                transactionList.map { transaction ->
                    TransactionResult(
                        id = transaction.id,
                        name = transaction.name,
                        type = transaction.type,
                        moneyAmount = transaction.moneyAmount,
                        remark = transaction.remark,
                        goalId = 0,
                        categoryId = transaction.categoryId,
                        categoryName = categoryList.find { category ->
                            category.id == transaction.categoryId
                        }?.name.toString()
                    )
                }
            }
        )
    }

}