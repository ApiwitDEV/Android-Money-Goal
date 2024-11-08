package com.overshoot.domain.usecase.initial

import com.overshoot.data.repository.CategoryRepository
import com.overshoot.data.repository.TransactionRepository

class LoadAllInitialDataUseCase(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke() {
        categoryRepository.getAllCategory()
        transactionRepository.getAllTransaction()
    }

}