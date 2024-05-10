package com.overshoot.data.datasource.local.transaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transaction_table")
    fun getTransaction(): Flow<List<TransactionEntity>>

    @Insert
    fun addTransaction(transaction:TransactionEntity)

    @Delete
    fun deleteTransaction(transaction:TransactionEntity)

}