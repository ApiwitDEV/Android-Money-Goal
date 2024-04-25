package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.runBlocking

class TransactionRepositoryImpl(
    private val fakeTransactionDataSource: StreamingDataSource<TransactionEntity>
): BaseRepository(), TransactionRepository {

    private var isSubscribe = false

    override suspend fun addTransaction(): ResultData<TransactionEntity> {
        return callRestFulApi {
            delay(20000)
            TransactionEntity(
                id = 5,
                name = "",
                createAt = "",
                updateAt = "",
//                categoryId = listOf(),
                moneyAmount = 5.5,
                type = "",
                remark = ""
            )
        }
    }

    override suspend fun subscribe(): Flow<TransactionEntity> {
        return subscribeStreamingData { onDataReceived ->
            fakeTransactionDataSource.bindData { transactionEntity ->
                onDataReceived(transactionEntity)
            }
        }
    }

    override suspend fun subscribe2(): Flow<TransactionEntity> {
        return flow {
            if (!isSubscribe) {
                fakeTransactionDataSource.bindData {
                    emit(it)
                }
            }
            isSubscribe = true
        }.flowOn(Dispatchers.IO)
    }

}