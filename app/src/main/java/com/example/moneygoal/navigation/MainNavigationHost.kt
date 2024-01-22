package com.example.moneygoal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moneygoal.component.dashboard.DashboardScreen
import com.example.moneygoal.component.home.ui.HomeScreen
import com.example.moneygoal.component.home.stateholder.viewmodel.GoalViewModel
import com.example.moneygoal.component.home.stateholder.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationHost(
    goalViewModel: GoalViewModel,
    transactionViewModel: TransactionViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainNavigationRoute.HomeScreen.name) {

        composable(route = MainNavigationRoute.HomeScreen.name) {
            HomeScreen(
                goalViewModel = goalViewModel,
                transactionViewModel = transactionViewModel,
                onGoto = {
                    CoroutineScope(context = Dispatchers.Main).launch {
                        delay(200)
                        navController.navigate(route = MainNavigationRoute.DashboardScreen.name)
                    }
                }
            )
        }

        composable(route = MainNavigationRoute.DashboardScreen.name) {
            DashboardScreen()
        }
    }
}