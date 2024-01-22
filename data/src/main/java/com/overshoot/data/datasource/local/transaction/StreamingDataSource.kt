package com.overshoot.data.datasource.local.transaction

interface StreamingDataSource<T> {

    suspend fun bindData(onDataReceived: suspend (T) -> Unit)

}