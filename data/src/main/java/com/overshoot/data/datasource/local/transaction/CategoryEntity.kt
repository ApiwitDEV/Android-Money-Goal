package com.overshoot.data.datasource.local.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,
    @ColumnInfo
    val description: String?
)
