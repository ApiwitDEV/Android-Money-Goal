package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.CategoryDao
import com.overshoot.data.datasource.local.transaction.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDao: CategoryDao
): BaseRepository() {

    fun subscribeCategory(): Flow<List<CategoryEntity>> {
        return categoryDao.subscribeCategory()
    }

    suspend fun getAllCategory(): ResultData<List<CategoryEntity>> {
        return callDB {
            categoryDao.getAllCategory()
        }
    }

}