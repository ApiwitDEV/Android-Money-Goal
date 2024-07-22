package com.overshoot.moneygoal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val  isLoading: State<Boolean> = _isLoading

    protected val _error = mutableStateOf("")
    val error: State<String> = _error

    protected fun <T: Any, R: Any> executeUseCase(
        mapToUIState: (T) -> R,
        action: suspend () -> ResultData<T>,
        onSuccess: (R) -> Unit = {},
        onFailure: (String) -> Unit = {},
        onConnectingNotAvailable: () -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            action()
                .onSuccess {
                    _isLoading.value = false
                    onSuccess(mapToUIState(it))
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

    protected fun <T, R> observeStreamingData(
        action: suspend () -> Flow<R>,
        mapToUIState: (R) -> T,
        onDataReceived: (T) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            action()
                .map {
                    mapToUIState(it)
                }
                .collect {
                    onDataReceived(it)
                }
        }
    }

}