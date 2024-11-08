package com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel

import androidx.compose.runtime.IntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCase
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(
    private val subscribeTransactionUseCase: SubscribeTransactionUseCase
): ViewModel() {

    private val _transaction = mutableStateListOf<TransactionUIState>()
    val transaction: List<TransactionUIState> = _transaction

    private val _incomeCount = mutableIntStateOf(0)
    val incomeCount: IntState = _incomeCount

    private val _expenseCount = mutableIntStateOf(0)
    val expenseCount: IntState = _expenseCount

    fun subscribe() {
        viewModelScope.launch(Dispatchers.IO) {
            subscribeTransactionUseCase.invoke()
                .catch {
                    it.message
                }
                .map {
                    it.map { item ->
                        TransactionUIState(
                            name = item.name,
                            value = item.moneyAmount,
                            remark = item.remark,
                            goalId = 0,
                            type = item.type,
                            category = item.categoryName,
                            isLoading = item.isLoading
                        )
                    }
                }
                .collect {
                    _transaction.clear()
                    _transaction.addAll(it)
                    _incomeCount.intValue = it.count { transaction ->
                        transaction.type == "Income"
                    }
                    _expenseCount.intValue = it.count {transaction ->
                        transaction.type == "Expense"
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}