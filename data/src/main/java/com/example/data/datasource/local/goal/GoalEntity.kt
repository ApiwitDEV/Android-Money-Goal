package com.example.data.datasource.local.goal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GoalEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("name")
    val name: String?,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("budget")
    val budget: Double?
)
