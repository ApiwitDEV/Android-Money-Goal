package com.overshoot.moneygoal.component.transactionhistory.stateholder.viewmodel

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.repository.TransactionRepository
import com.overshoot.domain.usecase.transaction.SubscribeTransactionUseCase
import com.overshoot.moneygoal.component.home.uistatemodel.TransactionUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(
    private val subscribeTransactionUseCase: SubscribeTransactionUseCase,
    private val transactionRepository: TransactionRepository
): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _transaction = mutableStateListOf<TransactionUIState>()
    val transaction: List<TransactionUIState> = _transaction

    private val _incomeCount = mutableIntStateOf(0)
    val incomeCount: IntState = _incomeCount

    private val _expenseCount = mutableIntStateOf(0)
    val expenseCount: IntState = _expenseCount

    private val _isSelectMode = mutableStateOf(false)
    val isSelectMode: State<Boolean> = _isSelectMode

    private var _haveClickedItem = mutableStateOf(false)
    val haveClickedItem: State<Boolean> = _haveClickedItem

    private val _haveSelectedItem = mutableStateOf(false)
    val haveSelectedItem: State<Boolean> = _haveSelectedItem

    fun subscribe() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            subscribeTransactionUseCase.invoke()
                .catch {
                    it.message
                }
                .map {
                    it.map { item ->
                        TransactionUIState(
                            id = item.id?:"",
                            name = item.name,
                            value = item.moneyAmount,
                            remark = item.remark,
                            goalId = 0,
                            type = item.type,
                            category = item.categoryName,
                            isSelected = false,
                            isEdit = false
                        )
                    }
                }
                .collect { result ->
                    _transaction.clear()
                    _transaction.addAll(result)
                    _incomeCount.intValue = result.count { transaction ->
                        transaction.type == "Income"
                    }
                    _expenseCount.intValue = result.count {transaction ->
                        transaction.type == "Expense"
                    }
                    _isLoading.value = false
                }
        }
    }

    fun onSelectMode() {
        _isSelectMode.value = true
        _haveSelectedItem.value = _transaction.any { it.isSelected }
    }

    fun onSelectAll() {
        _transaction.replaceAll {
            it.copy(isSelected = true)
        }
        _haveSelectedItem.value = true
    }

    fun clearAllSelectedStatus() {
        viewModelScope.launch(Dispatchers.Default) {
            _transaction.replaceAll {
                it.copy(isSelected = false)
            }
            delay(400)
            _isSelectMode.value = false
        }
        _haveSelectedItem.value = false
    }

    fun onSelectItem(transactionId: String) {
        _transaction.replaceAll {
            if (it.id == transactionId) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }
        _haveSelectedItem.value = _transaction.any { it.isSelected }
    }

    fun onClickToEdit(transactionId: String) {
        _transaction.replaceAll {
            if (it.id == transactionId) {
                val isEdit = !it.isEdit
                _haveClickedItem.value = isEdit
                it.copy(isEdit = isEdit)
            } else {
                it.copy(isEdit = false)
            }
        }
    }

    fun onEditTransaction(transactionId: String) {

    }

    fun cancelDelete() {
        _isSelectMode.value = false
        _haveSelectedItem.value = false
    }

    fun deleteTransaction() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.Default) {
            transactionRepository.deleteTransactions(
                _transaction.filter { it.isSelected }
                    .fold(listOf()) { acc, transaction ->
                        acc + transaction.id
                    }
            )
                .onSuccess {
                    _isLoading.value = false
                }
                .onFailure {
                    _isLoading.value = false
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}