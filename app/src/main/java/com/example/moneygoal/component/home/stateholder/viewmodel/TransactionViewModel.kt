package com.example.moneygoal.component.home.stateholder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.data.datasource.local.transaction.TransactionEntity
import com.example.domain.AddTransactionUseCase
import com.example.moneygoal.BaseViewModel
import com.example.moneygoal.component.home.uistatemodel.TransactionUIState

class TransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase
): BaseViewModel<TransactionEntity, TransactionUIState>() {

    private val _transaction = mutableStateOf<TransactionUIState?>(null)
    val transaction: State<TransactionUIState?> = _transaction

    private val _addTransactionSuccess = mutableStateOf<Boolean?>(null)
    val addTransactionSuccess: State<Boolean?> = _addTransactionSuccess

    override fun TransactionEntity.mapToUIState(): TransactionUIState {
        return TransactionUIState(
            id = this.id,
            label = this.label,
            cost = this.cost,
            remark = this.remark,
            goalId = this.goalId
        )
    }

    fun addTransaction() {
        executeUseCase(
            action = {
                addTransactionUseCase.addTransaction()
            },
            onLoading = {

            },
            onSuccess = {
                _transaction.value = it
            },
            onFailure = {
                _error.value = it
            },
            onConnectingNotAvailable = {
                _isConnectingLost.value = true
            }
        )
    }

    fun subscribe() {
        observeStreamingData(
            action = {
                addTransactionUseCase.subscribe()
            },
            onDataReceived = {
                _transaction.value = it
            }
        )
    }

    fun subscribe2() {
        observeStreamingData(
            action = {
                addTransactionUseCase.subscribe2()
            },
            onDataReceived = {
                _transaction.value = it
            }
        )
    }

}