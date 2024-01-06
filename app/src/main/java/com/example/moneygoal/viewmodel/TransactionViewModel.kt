package com.example.moneygoal.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionViewModel: ViewModel() {

    private val _addTransactionSuccess = mutableStateOf<Boolean?>(null)
    val addTransactionSuccess: State<Boolean?> = _addTransactionSuccess

    private val _addTransactionSuccess2 = MutableLiveData<Unit>()
    val addTransactionSuccess2: LiveData<Unit> = _addTransactionSuccess2

    fun addTransaction() {
        _addTransactionSuccess.value = true
        _addTransactionSuccess2.value = Unit
    }

    fun clearStatus() {
        _addTransactionSuccess.value = false
    }

}