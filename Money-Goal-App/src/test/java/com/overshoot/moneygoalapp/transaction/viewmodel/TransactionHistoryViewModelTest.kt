package com.overshoot.moneygoalapp.transaction.viewmodel

import com.overshoot.data.datasource.local.category.CategoryEntity
import com.overshoot.data.testrepository.FakeCategoryRepository
import com.overshoot.data.testrepository.FakeTransactionRepository
import com.overshoot.domain.model.TransactionResult
import com.overshoot.domain.usecase.initial.LoadAllInitialDataUseCase
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCase
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCaseImpl
import com.overshoot.moneygoalapp.component.transactionhistory.stateholder.viewmodel.TransactionHistoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionHistoryViewModelTest {

    private lateinit var viewModel: TransactionHistoryViewModel
    private lateinit var transactionRepository: FakeTransactionRepository
    private lateinit var categoryRepository: FakeCategoryRepository
    private lateinit var subscribeTransactionUseCase: SubscribeTransactionUseCase
    private lateinit var loadAllInitialDataUseCase: LoadAllInitialDataUseCase

//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        transactionRepository = FakeTransactionRepository()
        categoryRepository = FakeCategoryRepository()
        subscribeTransactionUseCase = SubscribeTransactionUseCaseImpl(
            transactionRepository = transactionRepository,
            categoryRepository = categoryRepository
        )
        loadAllInitialDataUseCase = LoadAllInitialDataUseCase(
            categoryRepository = categoryRepository,
            transactionRepository = transactionRepository
        )
        viewModel = TransactionHistoryViewModel(
            subscribeTransactionUseCase = subscribeTransactionUseCase,
            transactionRepository = transactionRepository
        )
        val defaultCategory = listOf(
            CategoryEntity(
                id = "1",
                name = "Category 1"
            ),
            CategoryEntity(
                id = "2",
                name = "Category 2"
            ),
            CategoryEntity(
                id = "3",
                name = "Category 3"
            )
        )
        categoryRepository.setDefaultCategories(defaultCategory)
        runBlocking { loadAllInitialDataUseCase.invoke() }
    }

    @Test
    fun getAllTransaction_noInitialTransaction_haveNoTransaction() {
        runTest {
            val result = mutableListOf<List<TransactionResult>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                subscribeTransactionUseCase.invoke().toList(result)
            }
            val actual = listOf(listOf<TransactionResult>())
            assertEquals(actual, result)
            println(result)
        }
    }

    @Test
    fun addTransaction_noInitialTransaction_haveAddedTransaction() {
        runTest {
            val result = mutableListOf<List<TransactionResult>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                subscribeTransactionUseCase.invoke().toList(result)
            }
            transactionRepository.addTransaction(
                name = "name",
                type = "type",
                value = 12.1,
                remark = "remark",
                categoryId = "1"
            )
            assertEquals(
                listOf(
                    listOf(),
                    listOf(TransactionResult(id="1", name="name", type="type", moneyAmount=12.1, remark="remark", goalId=0, categoryId="1", categoryName="Category 1"))
                ),
                result
            )

            transactionRepository.addTransaction(
                name = "name2",
                type = "type2",
                value = 1.11,
                remark = "",
                categoryId = "1"
            )
            assertEquals(
                listOf(
                    listOf(),
                    listOf(TransactionResult(id="1", name="name", type="type", moneyAmount=12.1, remark="remark", goalId=0, categoryId="1", categoryName="Category 1")),
                    listOf(
                        TransactionResult(id="1", name="name", type="type", moneyAmount=12.1, remark="remark", goalId=0, categoryId="1", categoryName="Category 1"),
                        TransactionResult(id="2", name="name2", type="type2", moneyAmount=1.11, remark="", goalId=0, categoryId="1", categoryName="Category 1")
                    )
                ),
                result
            )

            transactionRepository.addTransaction(
                name = "name3",
                type = "type",
                value = 1.1,
                remark = "",
                categoryId = "2"
            )
            assertEquals(
                listOf(
                    listOf(),
                    listOf(TransactionResult(id="1", name="name", type="type", moneyAmount=12.1, remark="remark", goalId=0, categoryId="1", categoryName="Category 1")),
                    listOf(
                        TransactionResult(id="1", name="name", type="type", moneyAmount=12.1, remark="remark", goalId=0, categoryId="1", categoryName="Category 1"),
                        TransactionResult(id="2", name="name2", type="type2", moneyAmount=1.11, remark="", goalId=0, categoryId="1", categoryName="Category 1")
                    ),
                    listOf(
                        TransactionResult(id="1", name="name", type="type", moneyAmount=12.1, remark="remark", goalId=0, categoryId="1", categoryName="Category 1"),
                        TransactionResult(id="2", name="name2", type="type2", moneyAmount=1.11, remark="", goalId=0, categoryId="1", categoryName="Category 1"),
                        TransactionResult(id="3", name="name3", type="type", moneyAmount=1.1, remark="", goalId=0, categoryId="2", categoryName="Category 2")
                    )
                ),
                result
            )
        }
    }

}