package com.overshoot.moneygoalapp.navigation.main

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.overshoot.moneygoalapp.AppStateHolder
import com.overshoot.moneygoalapp.component.account.AccountScreen
import com.overshoot.moneygoalapp.component.home.ui.HomeScreen
import com.overshoot.moneygoalapp.component.scanbill.stateholder.ScanBillViewModel
import com.overshoot.moneygoalapp.component.scanbill.ui.ScanBillScreen
import com.overshoot.moneygoalapp.component.transactionhistory.TransactionHistoryScreen
import com.overshoot.moneygoalapp.navigation.authentication.AuthenticationNavigationRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigationHost(
    appStateHolder: AppStateHolder,
    openAddTransactionSheet: () -> Unit,
    openAddGoalSheet: () -> Unit,
) {
    val scanBillViewModel = koinViewModel<ScanBillViewModel>()
    appStateHolder.mainNavController = rememberNavController()
    val navController = appStateHolder.mainNavController
    NavHost(
        navController = navController!!,
        startDestination = MainNavigationRoute.Home.name
    ) {

        composable(
            MainNavigationRoute.Home.name,
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            HomeScreen(
                goToDashboard = {
                    appStateHolder.navigateTo(AuthenticationNavigationRoute.Dashboard)
                },
                openGoalSheet =  openAddGoalSheet,
                goalPeriodList = listOf(),
                successGoalList = listOf(),
                failGoalList = listOf(),
                selectedGoalPeriod = "",
                onSelectPeriod = {},
                onClickGoal = {},
                openAddTransactionSheet = openAddTransactionSheet
            )
        }

        composable(
            MainNavigationRoute.ScanBill.name,
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            ScanBillScreen(scanBillViewModel = scanBillViewModel)
        }

        composable(
            MainNavigationRoute.TransactionHistory.name,
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            TransactionHistoryScreen()
        }

        composable(
            MainNavigationRoute.Account.name,
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            AccountScreen(appStateHolder)
        }

    }
}