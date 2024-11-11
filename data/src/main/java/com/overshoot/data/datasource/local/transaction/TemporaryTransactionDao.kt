package com.overshoot.data.datasource.local.transaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemporaryTransactionDao {

    @Query("SELECT * FROM temporary_transaction_table")
    fun getTemporaryTransaction(): Flow<List<TemporaryTransactionEntity>>

    @Insert
    suspend fun addTemporaryTransaction(temporaryTransaction: TemporaryTransactionEntity)

    @Delete
    suspend fun deleteTemporaryTransaction(temporaryTransaction: TemporaryTransactionEntity)

    @Query("DELETE FROM temporary_transaction_table")
    suspend fun deleteAll()

}