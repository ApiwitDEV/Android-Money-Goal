package com.overshoot.data.repository

import android.icu.util.Calendar
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TransactionDao
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.datasource.remote.MoneyGoalApiService
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionRequestBody
import com.overshoot.data.datasource.remote.model.transaction.PostTransactionResponse
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat

class TransactionRepository(
    private val fakeTransactionDataSource: StreamingDataSource<TransactionEntity>,
    private val transactionDao: TransactionDao,
    private val moneyGoalApiService: MoneyGoalApiService
): BaseRepository() {

    suspend fun addTransaction(
        name: String,
        categoryId: Int,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse> {
        return callRestApi {
            moneyGoalApiService.postTransaction(
                PostTransactionRequestBody(
                    name,
                    categoryId,
                    remark,
                    type,
                    value
                )
            )
        }.onSuccess {
            val data = it.data
            transactionDao.addTransaction(
                TransactionEntity(
                    name = data?.name,
                    createAt = data?.createAt,
                    updateAt = data?.updateAt,
                    categoryId = data?.categoryId,
                    moneyAmount = data?.moneyAmount,
                    type = data?.type,
                    remark = data?.remark,
                    isExistOnServer = true
                )
            )
        }.onFailure {
            val time = Calendar.getInstance().time
            transactionDao.addTransaction(
                TransactionEntity(
                    name = name,
                    createAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                    updateAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                    categoryId = categoryId,
                    moneyAmount = value,
                    type = type,
                    remark = remark,
                    isExistOnServer = false
                )
            )
        }
    }

    fun subscribe(): Flow<List<TransactionEntity>> {
        return transactionDao.getTransaction()
    }

//    override suspend fun subscribe(): Flow<TransactionEntity> {
//        return subscribeStreamingData { onDataReceived ->
//            fakeTransactionDataSource.bindData { transactionEntity ->
//                onDataReceived(transactionEntity)
//            }
//        }
//    }

}