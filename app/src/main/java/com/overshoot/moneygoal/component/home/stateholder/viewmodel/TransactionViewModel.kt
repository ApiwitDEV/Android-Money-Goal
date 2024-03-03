package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.domain.AddTransactionUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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
                    label = it.label,
                    cost = it.cost,
                    remark = it.remark,
                    goalId = it.goalId
                )
            },
            onLoading = {

            },
            onSuccess = {
                _x.value = it
                _transaction.value = it
            },
            onFailure = {
                mError.value = it
            },
            onConnectingNotAvailable = {
                mIsConnectingLost.value = true
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
                    label = it.label,
                    cost = it.cost,
                    remark = it.remark,
                    goalId = it.goalId
                )
            },
            onDataReceived = {
                _transaction.value = it
            }
        )
    }

}