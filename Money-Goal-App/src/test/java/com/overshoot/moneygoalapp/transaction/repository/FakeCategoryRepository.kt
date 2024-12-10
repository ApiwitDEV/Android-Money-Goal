package com.overshoot.moneygoalapp.transaction.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.category.CategoryEntity
import com.overshoot.data.datasource.remote.model.categories.GetCategoriesResponse
import com.overshoot.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class FakeCategoryRepository : CategoryRepository {
    override fun subscribeCategory(): Flow<List<CategoryEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategory(): ResultData<GetCategoriesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCategories() {
        TODO("Not yet implemented")
    }
}