package com.overshoot.moneygoalapp.component.home.stateholder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.domain.usecase.transaction.AddTransactionUseCase
import com.overshoot.moneygoalapp.component.home.uistatemodel.CategoryUIState
import com.overshoot.moneygoalapp.component.home.upstreamdatamodel.AddTransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _categoryList = MutableStateFlow<List<CategoryUIState>>(listOf())
    val categoryList = _categoryList.asStateFlow()

    private val _addTransactionLoading = MutableStateFlow(false)
    val addTransactionLoading = _addTransactionLoading.asStateFlow()

    fun addTransaction(transaction: AddTransactionData) {
        _addTransactionLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            addTransactionUseCase.invoke(
                name = transaction.name,
                categoryId = transaction.categoryId,
                remark = transaction.remark,
                type = transaction.type,
                value = transaction.value
            )
                .onSuccess {
                    _addTransactionLoading.value = false
                    Log.d("add_transaction", it.toString())
                }
                .onFailure {
                    _addTransactionLoading.value = false
                    Log.d("add_transaction", it)
                }
        }
    }

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.subscribeCategory()
                .collectLatest {
                    _categoryList.value = it.map { category ->
                        CategoryUIState(
                            id = category.id?:"",
                            name = category.name?:""
                        )
                    }
                }
        }
    }

}