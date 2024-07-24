package com.overshoot.moneygoal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.overshoot.data.datasource.remote.network.InternetConnectivity
import com.overshoot.moneygoal.navigation.MainNavigationRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    scope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    internetState: Flow<InternetConnectivity.InternetConnectivityState>,
    isSigned: Boolean
): AppStateHolder {
    val appState = remember {
        AppStateHolder(
            scope,
            navController,
            internetState,
            isSigned
        )
    }
    return appState
}

class AppStateHolder(
    scope: CoroutineScope,
    val navController: NavHostController,
    internetState: Flow<InternetConnectivity.InternetConnectivityState>,
    val isSigned: Boolean
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

    fun navigateTo(destination: MainNavigationRoute) {
        navController.navigate(destination.name) {
            popUpTo(navController.graph.startDestinationId)
        }
    }

}