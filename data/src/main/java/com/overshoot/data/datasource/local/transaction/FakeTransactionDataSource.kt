package com.overshoot.data.datasource.local.transaction

import kotlinx.coroutines.delay

class FakeTransactionDataSource: StreamingDataSource<TransactionEntity> {

    override suspend fun bindData(onDataReceived: suspend ( TransactionEntity ) -> Unit) {
        var i = 0
        while (true) {
            delay(30000)
            onDataReceived(
                TransactionEntity(
                    id = i,
                    name = "",
                    type = "cost",
                    createAt = "",
                    updateAt = "",
                    moneyAmount = 30.0,
//                    categoryId = listOf(),
                    remark = ""
                    )
            )
            i++
        }
    }

}