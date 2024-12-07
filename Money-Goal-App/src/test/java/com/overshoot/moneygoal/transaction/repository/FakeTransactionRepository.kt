package com.overshoot.moneygoal.transaction.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import com.overshoot.data.repository.TransactionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class FakeTransactionRepository: TransactionRepository {

    override suspend fun addTransaction(
        name: String,
        categoryId: String,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTransaction() {
        println()
    }

    override fun subscribe(): Flow<List<TransactionEntity>> {
        return flow {
            g.gen {
                emit(
                    listOf(
                        TransactionEntity(
                            id = "1",
                            name = "",
                            type = "",
                            createAt = "",
                            updateAt = "",
                            moneyAmount = 2.2,
                            categoryId = "",
                            remark = ""
                        )
                    )
                )
            }
//            awaitClose { trySend(listOf()) }
        }
//        return callbackFlow {
//            val l1 = listOf(TransactionEntity(
//                id = "1",
//                name = "",
//                type = "",
//                createAt = "",
//                updateAt = "",
//                moneyAmount = 2.2,
//                categoryId = "",
//                remark = ""
//            ),
//                TransactionEntity(
//                    id = "1",
//                    name = "",
//                    type = "",
//                    createAt = "",
//                    updateAt = "",
//                    moneyAmount = 2.2,
//                    categoryId = "",
//                    remark = ""
//                ))
//            delay(2000)
//            send(l1)
//            val l2 = listOf(TransactionEntity(
//                id = "2",
//                name = "",
//                type = "",
//                createAt = "",
//                updateAt = "",
//                moneyAmount = 2.2,
//                categoryId = "",
//                remark = ""
//            ),
//                TransactionEntity(
//                    id = "2",
//                    name = "",
//                    type = "",
//                    createAt = "",
//                    updateAt = "",
//                    moneyAmount = 2.2,
//                    categoryId = "",
//                    remark = ""
//                ))
//            delay(2000)
//            send(l2)
//            awaitClose {  }
//        }
    }

    override suspend fun deleteAllTransactions() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransactions(transactionIds: List<String>): ResultData<String> {
        TODO("Not yet implemented")
    }

    object g {
        suspend fun gen(block: suspend () -> Unit) {
            repeat (3) {
                delay(1000)
                block()
            }
        }
    }

}