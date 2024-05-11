package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.domain.usecase.AddTransactionUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.CategoryUIState
import com.overshoot.moneygoal.component.home.upstreamdatamodel.AddTransactionData

class HomeTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categoryRepository: CategoryRepository
): BaseViewModel() {

    private val _categoryList = mutableStateListOf<CategoryUIState>()
    val category: List<CategoryUIState> = _categoryList

    fun addTransaction(transaction: AddTransactionData) {
        executeUseCase(
            action = {
                addTransactionUseCase.addTransaction(
                    name = transaction.name,
                    categoryId = transaction.categoryId,
                    remark = transaction.remark,
                    type = transaction.type,
                    value = transaction.value
                )
            },
            mapToUIState = {}
        )
    }

    fun getAllCategory() {
        executeUseCase(
            action = {
                categoryRepository.getAllCategory()
            },
            mapToUIState = {
                it.map { category ->
                    CategoryUIState(
                        id = category.id,
                        name = category.name?:""
                    )
                }
            },
            onSuccess = {
                _categoryList.clear()
                _categoryList.addAll(it)
            },
            onFailure = {

            }
        )
    }

}