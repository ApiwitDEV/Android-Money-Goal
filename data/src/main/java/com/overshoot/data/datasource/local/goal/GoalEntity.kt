package com.overshoot.data.datasource.local.goal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_table")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,
    @ColumnInfo
    val name: String?,
    @ColumnInfo
    val description: String?,
    @ColumnInfo("create_at")
    val createAt: String?,
    @ColumnInfo("update_at")
    val updateAt: String?,
    @ColumnInfo
    val objective: String?,
    @ColumnInfo
    val period: String?,
//    @ColumnInfo("support_to")
//    val supportTo: List<Int>?,
    @ColumnInfo
    val target: Double?,
    @ColumnInfo
    val cost: Double?,
    @ColumnInfo
    val income: Double?,
//    @ColumnInfo("transaction_ids")
//    val transactionIds: List<Int>?
)
