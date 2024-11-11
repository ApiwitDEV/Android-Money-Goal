package com.overshoot.data.datasource.local.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_table")
    fun subscribeCategory(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category_table")
    suspend fun getAllCategory(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM category_table")
    suspend fun deleteAll()

}