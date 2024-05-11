package com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel

import androidx.compose.runtime.IntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import com.overshoot.domain.usecase.SubscribeTransactionUseCase
import com.overshoot.moneygoal.BaseViewModel
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import kotlinx.coroutines.flow.catch

class TransactionHistoryViewModel(
    private val subscribeTransactionUseCase: SubscribeTransactionUseCase
): BaseViewModel() {

    private val _transaction = mutableStateListOf<TransactionUIState>()
    val transaction: List<TransactionUIState> = _transaction

    private val _incomeCount = mutableIntStateOf(0)
    val incomeCount: IntState = _incomeCount

    private val _expenseCount = mutableIntStateOf(0)
    val expenseCount: IntState = _expenseCount

    fun subscribe() {
        observeStreamingData(
            action = {
                subscribeTransactionUseCase()
                    .catch {
                        it.message
                    }
            },
            mapToUIState = {
                it.map { item ->
                    TransactionUIState(
                        id = item.id,
                        name = item.name,
                        value = item.value,
                        remark = item.remark,
                        goalId = item.id,
                        type = item.type,
                        category = item.categoryName
                    )
                }
            },
            onDataReceived = {
                _transaction.clear()
                _transaction.addAll(it)
                _incomeCount.intValue = it.count { transaction ->
                    transaction.type == "Income"
                }
                _expenseCount.intValue = it.count {transaction ->
                    transaction.type == "Expense"
                }
            }
        )
    }

}