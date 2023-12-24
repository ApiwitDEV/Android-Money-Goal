package com.example.moneygoal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moneygoal.ui.page.dashboard.DashboardScreen
import com.example.moneygoal.ui.page.home.HomeScreen
import com.example.moneygoal.viewmodel.GoalViewModel

@Composable
fun NavigationHost(
    goalViewModel: GoalViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationRoute.HomeScreen.name) {

        composable(route = NavigationRoute.HomeScreen.name) {
            HomeScreen(
                goalViewModel = goalViewModel,
                onGoto = {
                    navController.navigate(route = NavigationRoute.DashboardScreen.name)
                }
            )
        }

        composable(route = NavigationRoute.DashboardScreen.name) {
            DashboardScreen()
        }
    }
}