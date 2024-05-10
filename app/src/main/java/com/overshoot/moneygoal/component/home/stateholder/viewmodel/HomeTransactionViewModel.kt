package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.overshoot.domain.AddTransactionUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import com.overshoot.moneygoal.component.home.upstreamdatamodel.AddTransactionData
import kotlinx.coroutines.flow.catch

class HomeTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase
): BaseViewModel() {

    private val _transaction = mutableStateOf<List<TransactionUIState>>(listOf())
    val transaction: State<List<TransactionUIState>> = _transaction

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

    fun subscribe() {
        observeStreamingData(
            action = {
                addTransactionUseCase.subscribe2()
                    .catch {
                        it.message
                    }
            },
            mapToUIState = {
                it.map { item ->
                    TransactionUIState(
                        id = item.id,
                        name = item.name,
                        value = item.moneyAmount,
                        remark = item.remark,
                        goalId = item.id,
                        type = item.type
                    )
                }
            },
            onDataReceived = {
                _transaction.value = it
            }
        )
    }

}