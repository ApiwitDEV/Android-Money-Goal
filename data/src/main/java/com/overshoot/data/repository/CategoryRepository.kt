package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.category.CategoryDao
import com.overshoot.data.datasource.local.category.CategoryEntity
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.datasource.remote.MoneyGoalApiService
import com.overshoot.data.datasource.remote.model.categories.GetCategoriesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val moneyGoalApiService: MoneyGoalApiService
): BaseRepository() {

    fun subscribeCategory(): Flow<List<CategoryEntity>> {
        return categoryDao.subscribeCategory()
    }

    suspend fun getAllCategory(): ResultData<GetCategoriesResponse> {
        return callRestApi { moneyGoalApiService.getCategories() }
            .onSuccess {
                CoroutineScope(Dispatchers.Default).launch {
                    it.categories.forEach { category ->
                        categoryDao.addCategory(
                            CategoryEntity(
                                id = category.id,
                                name = category.name
                            )
                        )
                    }
                }
            }
            .onFailure {
            }
    }

    suspend fun deleteAllCategories() {
        categoryDao.deleteAll()
    }

}