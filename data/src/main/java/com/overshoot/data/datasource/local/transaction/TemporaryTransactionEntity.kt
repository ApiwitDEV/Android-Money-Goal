package com.overshoot.data.datasource.local.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temporary_transaction_table")
data class TemporaryTransactionEntity(
    @ColumnInfo("running_number")
    @PrimaryKey(autoGenerate = true)
    val runningNumber: Int = 0,
    @ColumnInfo
    val name: String?,
    @ColumnInfo
    val type: String?,
    @ColumnInfo("create_at")
    val createAt: String?,
    @ColumnInfo("update_at")
    val updateAt: String?,
    @ColumnInfo("money_amount")
    val moneyAmount: Double?,
    @ColumnInfo("category_id")
    val categoryId: String?,
    @ColumnInfo
    val remark: String?
)
