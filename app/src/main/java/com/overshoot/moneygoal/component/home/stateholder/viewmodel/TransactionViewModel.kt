package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.overshoot.domain.AddTransactionUseCase
import com.overshoot.moneygoal.AppStateHolder
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

class TransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase
): BaseViewModel() {

    private val _transaction = mutableStateOf<TransactionUIState?>(null)
    val transaction: State<TransactionUIState?> = _transaction

    private val _addTransactionSuccess = mutableStateOf<Boolean?>(null)
    val addTransactionSuccess: State<Boolean?> = _addTransactionSuccess

    private val _x = MutableStateFlow(TransactionUIState(id = 0, cost = 0.0, goalId = 0, label = "", remark = ""))
    val x: StateFlow<TransactionUIState> = _x

    fun addTransaction() {
        executeUseCase(
            action = {
                addTransactionUseCase.addTransaction()
            },
            mapToUIState = {
                TransactionUIState(
                    id = it.id,
                    label = it.name,
                    cost = it.moneyAmount,
                    remark = it.remark,
                    goalId = it.id
                )
            },
            onSuccess = {
                _x.value = it
                _transaction.value = it
            },
            onFailure = {
                _error.value = it
            },
            onConnectingNotAvailable = {
                AppStateHolder.getInstant()?.showNoInternetStatus()
            }
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
                TransactionUIState(
                    id = it.id,
                    label = it.name,
                    cost = it.moneyAmount,
                    remark = it.remark,
                    goalId = it.id
                )
            },
            onDataReceived = {
                _transaction.value = it
            }
        )
    }

}