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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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

    private val _isLoadTransaction = MutableSharedFlow<Boolean>(1)
    val isLoadTransaction: SharedFlow<Boolean> = _isLoadTransaction.asSharedFlow()

    suspend fun addTransaction(
        name: String,
        categoryId: String,
        remark: String,
        type: String,
        value: Double
    ): ResultData<PostTransactionResponse> {
        _isLoadTransaction.emit(true)
        return callRestApi {
            moneyGoalApiService.postTransaction(
                PostTransactionRequestBody(
                    name = name,
                    categoryId = categoryId,
                    remark = remark,
                    type = type,
                    moneyAmount = value
                )
            )
        }.onSuccess {
            CoroutineScope(Dispatchers.Default).launch {
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
                _isLoadTransaction.emit(false)
            }
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
            _isLoadTransaction.emit(false)
        }
    }

    suspend fun getAllTransaction() {
        _isLoadTransaction.emit(true)
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
                _isLoadTransaction.emit(false)
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