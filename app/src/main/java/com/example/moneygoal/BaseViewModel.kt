package com.example.moneygoal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.ResultData
import com.example.data.datasource.onFailure
import com.example.data.datasource.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseViewModel<R: Any,T: Any>: ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val  isLoading: State<Boolean> = _isLoading

    protected val _error = mutableStateOf("")
    val error: State<String> = _error

    protected val _isConnectingLost = mutableStateOf(false)
    val isConnectingLost: State<Boolean> = _isConnectingLost

    abstract fun R.mapToUIState(): T

    protected fun executeUseCase(
        onLoading: () -> Unit = {},
        action: suspend () -> ResultData<R>,
        onSuccess: (T) -> Unit,
        onFailure: (String) -> Unit,
        onConnectingNotAvailable: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            onLoading()
            action()
                .onSuccess {
                    _isLoading.value = false
                    onSuccess(it.mapToUIState())
                }
                .onFailure {
                    _isLoading.value = false
                    if (it == "Connectivity Lost") {
                        onConnectingNotAvailable()
                    } else {
                        onFailure(it)
                    }
                }
        }
    }

    protected fun observeStreamingData(
        action: suspend () -> Flow<R>,
        onDataReceived: (T) -> Unit
    ) {
        viewModelScope.launch {
            action()
                .map {
                    it.mapToUIState()
                }
                .collect {
                    onDataReceived(it)
                }
        }
    }

}