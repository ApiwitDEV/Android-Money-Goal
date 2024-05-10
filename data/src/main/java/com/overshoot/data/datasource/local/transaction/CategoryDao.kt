package com.overshoot.data.datasource.local.transaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_table")
    fun getCategory(): Flow<CategoryEntity>

    @Insert
    fun addCategory(category: CategoryEntity)

    @Delete
    fun deleteCategory(category: CategoryEntity)
}