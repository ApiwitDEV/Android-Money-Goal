package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.overshoot.data.repository.CategoryRepository
import com.overshoot.domain.usecase.transaction.AddTransactionUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.CategoryUIState
import com.overshoot.moneygoal.component.home.upstreamdatamodel.AddTransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            var x = 0
            callbackFlow {
                launch(Dispatchers.IO) {
                    Log.d("pusher log", "execute x")
                    delay(8000)
                    send(5)
                    Log.d("pusher log", "execute x complete")
                }
                launch(Dispatchers.IO) {
                    Log.d("pusher log", "execute y")
                    delay(10000)
                    send(10)
                    Log.d("pusher log", "execute y complete")
                }
                awaitClose {
                    Log.d("pusher log", "awaitClose")
                }
            }.collectIndexed { index, item ->
                    if (index == 0) {
                        x = item
                        Log.d("pusher log", "$item")
                    } else {
                        Log.d("pusher log", "$x , $item")
                    }
                }
        }
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