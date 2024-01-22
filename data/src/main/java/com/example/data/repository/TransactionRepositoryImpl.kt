package com.example.data.repository

import com.example.data.datasource.ResultData
import com.example.data.datasource.local.transaction.StreamingDataSource
import com.example.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class TransactionRepositoryImpl(
    private val fakeTransactionDataSource: StreamingDataSource<TransactionEntity>
): BaseRepository(), TransactionRepository {

    override suspend fun addTransaction(): ResultData<TransactionEntity> {
        return callRestFulApi {
            delay(5000)
            TransactionEntity(
                id = 5,
                label = "",
                cost = 50.0,
                remark = "",
                goalId = 5
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
        return callbackFlow {
            fakeTransactionDataSource.bindData {
                trySend(it)
            }
            awaitClose {  }
        }
    }

}