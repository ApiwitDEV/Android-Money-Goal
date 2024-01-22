package com.example.data.datasource.local.transaction

import kotlinx.coroutines.delay

class FakeTransactionDataSource: StreamingDataSource<TransactionEntity> {

    override suspend fun bindData(onDataReceived: suspend ( TransactionEntity ) -> Unit) {
        var i = 0
        while (true) {
            delay(5000)
            onDataReceived(TransactionEntity(
                id = i,
                label = "",
                cost = i*10.0,
                remark = "",
                goalId = i
            ))
            i++
        }
    }

}