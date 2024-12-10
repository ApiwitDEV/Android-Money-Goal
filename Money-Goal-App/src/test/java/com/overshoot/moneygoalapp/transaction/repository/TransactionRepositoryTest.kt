package com.overshoot.moneygoalapp.transaction.repository

import com.overshoot.data.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TransactionRepositoryTest {

    private lateinit var transactionRepository: TransactionRepository

    @Before
    fun setup() {
        transactionRepository = FakeTransactionRepository()
    }

    @Test
    fun subscribe() {
        runBlocking {
            transactionRepository.subscribe().collect {
                it
            }
            delay(5000)
        }
    }

}