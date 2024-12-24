package com.overshoot.data.testrepository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import com.overshoot.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeTransactionRepository: TransactionRepository {

    private val transactionIds = mutableListOf("0")
    private val transactions = mutableListOf<TransactionEntity>()
    private val transactionsFlow = MutableSharedFlow<List<TransactionEntity>>(1)

    override suspend fun addTransaction(
        name: String,
        categoryId: String,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse> {
        val id = (transactionIds.last().toInt() + 1).toString()
        transactionIds.add(id)
        transactions.add(
            TransactionEntity(
                id = id,
                name = name,
                type = type,
                createAt = "",
                updateAt = "",
                moneyAmount = value,
                categoryId = categoryId,
                remark = remark
            )
        )
        transactionsFlow.emit(transactions)
        return Success(PostTransactionResponse(transaction = null, message = "Success"))
    }

    override suspend fun getAllTransaction() {
        transactionsFlow.emit(transactions)
    }

    override fun subscribe(): Flow<List<TransactionEntity>> {
        return transactionsFlow.asSharedFlow()
    }

    override suspend fun deleteAllTransactions() {
        transactionIds.clear()
        transactions.clear()
        transactionsFlow.emit(emptyList())
    }

    override suspend fun deleteTransactions(transactionIds: List<String>): ResultData<String> {
        this.transactionIds.removeAll(transactionIds)
        transactions.removeAll { it.id in transactionIds }
        transactionsFlow.emit(transactions)
        return Success("Success")
    }
}