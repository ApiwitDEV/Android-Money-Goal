package com.overshoot.data.datasource.local.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("category_table")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo
    val id: String,
    @ColumnInfo
    val name: String?
)
