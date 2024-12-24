package com.overshoot.data.testrepository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import com.overshoot.data.datasource.local.category.CategoryEntity
import com.overshoot.data.datasource.remote.model.categories.CategoryData
import com.overshoot.data.datasource.remote.model.categories.GetCategoriesResponse
import com.overshoot.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeCategoryRepository: CategoryRepository {

    private val categoryIds = mutableListOf<String>()
    private val categories = mutableListOf<CategoryEntity>()
    private val categoriesFlow = MutableSharedFlow<List<CategoryEntity>>(1)

    fun setDefaultCategories(list: List<CategoryEntity>) {
        categoryIds.addAll(list.map { it.id })
        categories.addAll(list)
        categoriesFlow.tryEmit(categories)
    }

    override fun subscribeCategory(): Flow<List<CategoryEntity>> {
        return categoriesFlow.asSharedFlow()
    }

    override suspend fun getAllCategory(): ResultData<GetCategoriesResponse> {
        categoriesFlow.emit(categories)
        return Success(GetCategoriesResponse(categories = categories.map { CategoryData(id = it.id, name = it.name?:"") }, message = "Success"))
    }

    override suspend fun deleteAllCategories() {
        TODO("Not yet implemented")
    }
}