package com.overshoot.data.repository

import android.icu.util.Calendar
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionDao
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat

class TransactionRepositoryImpl(
    private val fakeTransactionDataSource: StreamingDataSource<TransactionEntity>,
    private val transactionDao: TransactionDao
): BaseRepository(), TransactionRepository {

    private var isSubscribe = false

    override suspend fun addTransaction(
        name: String,
        categoryId: Int,
        remark: String,
        type: String,
        value: Double
    ): ResultData<Unit> {
        return callDB {
            val time = Calendar.getInstance().time
            transactionDao.addTransaction(
                TransactionEntity(
                    name = name,
                    createAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                    updateAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
//                categoryId = listOf(),
                    moneyAmount = value,
                    type = type,
                    remark = remark
                )
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

    override suspend fun subscribe2(): Flow<List<TransactionEntity>> {
        return transactionDao.getTransaction()
//        return flow {
//            if (!isSubscribe) {
//                fakeTransactionDataSource.bindData {
//                    emit(it)
//                }
//            }
//            isSubscribe = true
//        }.flowOn(Dispatchers.IO)
    }

}