package com.overshoot.moneygoal.transaction.viewmodel

import com.overshoot.data.repository.CategoryRepository
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCase
import com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import com.overshoot.moneygoal.transaction.repository.FakeCategoryRepository
import com.overshoot.moneygoal.transaction.repository.FakeTransactionRepository
import org.junit.Before
import org.junit.Test

class TransactionViewModelTest {

    private lateinit var viewModel: TransactionHistoryViewModel
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var subscribeTransactionUseCase: SubscribeTransactionUseCase

    @Before
    fun setup() {
        transactionRepository = FakeTransactionRepository()
        categoryRepository = FakeCategoryRepository()
        subscribeTransactionUseCase = SubscribeTransactionUseCase(
            transactionRepository = transactionRepository,
            categoryRepository = categoryRepository
        )
        viewModel = TransactionHistoryViewModel(
            subscribeTransactionUseCase = subscribeTransactionUseCase,
            transactionRepository = transactionRepository
        )
    }

    @Test
    fun x() {
        viewModel.deleteTransaction()
    }

}