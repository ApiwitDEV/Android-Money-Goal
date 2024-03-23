package com.overshoot.moneygoal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.overshoot.data.datasource.remote.network.Connectivity
import kotlinx.coroutines.flow.StateFlow

class AppStateHolder {

    companion object {
        private var instant: AppStateHolder? = null
        fun getInstant(): AppStateHolder? {
            if (instant == null) {
                instant = AppStateHolder()
            }
            return instant
        }
    }

    private val _isShowNoInternetDrawer = mutableStateOf(true)
    val isShowNoInternetDrawer: State<Boolean> = _isShowNoInternetDrawer

    suspend fun collectInternetState(
        state: StateFlow<Connectivity.ConnectivityState>,
        onInternetAvailable:  suspend () -> Unit,
        onNoInternet: suspend () -> Unit
    ) {
        state.collect {
            when(it) {
                Connectivity.ConnectivityState.Available -> {
                    _isShowNoInternetDrawer.value = true
                    onInternetAvailable()
                }
                Connectivity.ConnectivityState.Losing -> {
                    _isShowNoInternetDrawer.value = false
                    onNoInternet()
                }
            }
        }
    }

    fun showNoInternetStatus() {
        _isShowNoInternetDrawer.value = true
        _isShowNoInternetDrawer.value = false
    }

    fun hideNoInternetState() {
        _isShowNoInternetDrawer.value = true
    }

}