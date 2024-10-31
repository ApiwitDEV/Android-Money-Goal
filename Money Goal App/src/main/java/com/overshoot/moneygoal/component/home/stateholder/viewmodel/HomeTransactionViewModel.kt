package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.domain.usecase.transaction.AddTransactionUseCase
import com.overshoot.moneygoal.component.home.uistatemodel.CategoryUIState
import com.overshoot.moneygoal.component.home.upstreamdatamodel.AddTransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _categoryList = MutableStateFlow<List<CategoryUIState>>(listOf())
    val category = _categoryList.asStateFlow()

    private val _addTransactionLoading = MutableStateFlow(false)
    val addTransactionLoading = _addTransactionLoading.asStateFlow()

    fun addTransaction(transaction: AddTransactionData) {
        viewModelScope.launch(Dispatchers.IO) {
            addTransactionUseCase.invoke(
                name = transaction.name,
                categoryId = transaction.categoryId,
                remark = transaction.remark,
                type = transaction.type,
                value = transaction.value
            )
                .onSuccess {
                    Log.d("add_transaction", it.toString())
                }
                .onFailure {
                    Log.d("add_transaction", it)
                }
        }
    }

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.getAllCategory()
                .onSuccess {
                    _categoryList.value = it.map { category ->
                        CategoryUIState(
                            id = category.id,
                            name = category.name?:""
                        )
                    }
                }
                .onFailure {  }
        }
    }

}