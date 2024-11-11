package com.overshoot.moneygoal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.overshoot.data.datasource.remote.network.InternetConnectivity
import com.overshoot.moneygoal.navigation.authentication.AuthenticationNavigationRoute
import com.overshoot.moneygoal.navigation.main.MainNavigationRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    scope: CoroutineScope = rememberCoroutineScope(),
    authenticationNavController: NavHostController = rememberNavController(),
    internetState: Flow<InternetConnectivity.InternetConnectivityState>
): AppStateHolder {
    val appState = remember {
        AppStateHolder(
            scope,
            authenticationNavController,
            internetState
        )
    }
    return appState
}

class AppStateHolder(
    scope: CoroutineScope,
    val authenticationNavController: NavHostController,
    internetState: Flow<InternetConnectivity.InternetConnectivityState>
) {
    var mainNavController: NavHostController? = null
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

    fun clearBackStackAndNavigateTo(destination: AuthenticationNavigationRoute) {
        authenticationNavController.navigate(destination.name) {
            popUpTo(0)
        }
    }

    fun clearBackStackAndNavigateTo(destination: MainNavigationRoute) {
        if (mainNavController?.currentDestination?.route == destination.name) {
            return
        }
        mainNavController?.navigate(destination.name) {
            popUpTo(0)
        }
    }

    fun navigateTo(destination: AuthenticationNavigationRoute) {
        authenticationNavController.navigate(destination.name)
    }

//    fun setMainNavController(navController: NavHostController) {
//        mainNavController = navController
//    }

}