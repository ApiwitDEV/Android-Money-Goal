package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.category.CategoryEntity
import com.overshoot.data.datasource.remote.model.categories.GetCategoriesResponse
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun subscribeCategory(): Flow<List<CategoryEntity>>

    suspend fun getAllCategory(): ResultData<GetCategoriesResponse>

    suspend fun deleteAllCategories()
}