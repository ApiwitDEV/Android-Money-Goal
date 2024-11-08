package com.overshoot.data.repository

import android.icu.util.Calendar
import android.os.Build
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.StreamingDataSource
import com.overshoot.data.datasource.local.transaction.TemporaryTransactionDao
import com.overshoot.data.datasource.local.transaction.TemporaryTransactionEntity
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
    private val temporaryTransactionDao: TemporaryTransactionDao,
    private val moneyGoalApiService: MoneyGoalApiService
): BaseRepository() {

    suspend fun addTransaction(
        name: String,
        categoryId: String,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse> {
        Result
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
            val responseData = it.transaction
            transactionDao.addTransaction(
                TransactionEntity(
                    id = responseData?.id?:"",
                    name = responseData?.name,
                    createAt = responseData?.createAt,
                    updateAt = responseData?.updateAt,
                    categoryId = responseData?.categoryId,
                    moneyAmount = responseData?.moneyAmount,
                    type = responseData?.type,
                    remark = responseData?.remark
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

            temporaryTransactionDao.addTemporaryTransaction(
                TemporaryTransactionEntity(
                    name = name,
                    createAt = timestamp,
                    updateAt = timestamp,
                    categoryId = categoryId,
                    moneyAmount = value,
                    type = type,
                    remark = remark
                )
            )
        }
    }

    suspend fun getAllTransaction() {
        callRestApi { moneyGoalApiService.getTransactions() }
            .onSuccess {
                it.transactions.forEach { transaction ->
                    transactionDao.addTransaction(
                        TransactionEntity(
                            id = transaction.id,
                            name = transaction.name,
                            createAt = transaction.createAt,
                            updateAt = transaction.updateAt,
                            categoryId = transaction.categoryId,
                            moneyAmount = transaction.moneyAmount,
                            type = transaction.type,
                            remark = transaction.remark
                        )
                    )
                }
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