package com.overshoot.data.datasource.local.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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
    val categoryId: Int?,
    @ColumnInfo
    val remark: String?
)