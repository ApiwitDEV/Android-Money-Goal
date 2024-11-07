package com.overshoot.data.repository

import android.icu.util.Calendar
import android.os.Build
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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
            val timestamp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Get the current date and time with the system's local time zone
                val localTime = ZonedDateTime.now()
                // Format it as an ISO 8601 string
                localTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            } else {
                val time = Calendar.getInstance().time
                SimpleDateFormat("yyyy-MM-DDTHH:MM:SS.SSSZ", Locale.getDefault()).format(time)
            }

            transactionDao.addTransaction(
                TransactionEntity(
                    name = name,
                    createAt = timestamp,
                    updateAt = timestamp,
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