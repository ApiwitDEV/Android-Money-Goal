package com.overshoot.moneygoal.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.overshoot.moneygoal.component.dashboard.DashboardScreen
import com.overshoot.moneygoal.component.home.ui.HomeScreen
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeGoalDetailViewModel
import com.overshoot.moneygoal.component.home.stateholder.viewmodel.HomeTransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationHost(
    homeGoalDetailViewModel: HomeGoalDetailViewModel,
    homeTransactionViewModel: HomeTransactionViewModel,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainNavigationRoute.HomeScreen.name) {

        composable(route = MainNavigationRoute.HomeScreen.name) {
            HomeScreen(
                homeGoalDetailViewModel = homeGoalDetailViewModel,
                homeTransactionViewModel = homeTransactionViewModel,
                onGoto = {
                    CoroutineScope(context = Dispatchers.Main).launch {
                        delay(200)
                        navController.navigate(route = MainNavigationRoute.DashboardScreen.name)
                    }
                },
                onSignOut = onSignOut
            )
        }

        composable(route = MainNavigationRoute.DashboardScreen.name) {
            DashboardScreen()
        }
    }
}