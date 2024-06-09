package com.overshoot.moneygoal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.overshoot.data.datasource.remote.network.InternetConnectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    scope: CoroutineScope = rememberCoroutineScope(),
    internetState: Flow<InternetConnectivity.InternetConnectivityState>
): AppStateHolder {
    val appState = remember {
        AppStateHolder(scope,internetState)
    }
    return appState
}

class AppStateHolder(
    scope: CoroutineScope,
    internetState: Flow<InternetConnectivity.InternetConnectivityState>
) {

    private val _isShowNoInternetDrawer = mutableStateOf<Boolean?>(null)
    val isShowNoInternetDrawer: State<Boolean?> = _isShowNoInternetDrawer

    init {
        scope.launch {
            internetState.collect {
                _isShowNoInternetDrawer.value = it == InternetConnectivity.InternetConnectivityState.Losing
            }
        }
    }

    fun setIsShowNoInternetDrawer(value: Boolean) {
        _isShowNoInternetDrawer.value = value
    }

}