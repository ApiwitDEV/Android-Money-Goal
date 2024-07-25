package com.overshoot.moneygoal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authentication.stateholder.AuthenticationViewModel
import com.example.authentication.ui.SignInScreen
import com.example.authentication.ui.SignUpScreen
import com.example.authentication.ui.VerificationCodeScreen
import com.overshoot.moneygoal.AppStateHolder
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
    appStateHolder: AppStateHolder,
    authenticationViewModel: AuthenticationViewModel,
    homeGoalDetailViewModel: HomeGoalDetailViewModel,
    homeTransactionViewModel: HomeTransactionViewModel,
    onSignOut: () -> Unit,
    onCloseApp: () -> Unit
) {
    val navController = appStateHolder.navController
    NavHost(
        navController = navController,
        startDestination = if (authenticationViewModel.isSigned()) MainNavigationRoute.HomeScreen.name else MainNavigationRoute.Login.name
    ) {

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
                onBackPressed = {
                    onCloseApp()
                },
                onSignOut = onSignOut
            )
        }

        composable(route = MainNavigationRoute.DashboardScreen.name) {
            DashboardScreen()
        }

        composable(MainNavigationRoute.Login.name) {
            SignInScreen(
                authenticationViewModel = authenticationViewModel,
                onSignUpClicked = {
                    appStateHolder.navigateTo(MainNavigationRoute.Register)
                },
                onNavigateBack = onCloseApp
            )
        }

        composable(MainNavigationRoute.Register.name) {
            SignUpScreen(
                onBack = {
                    appStateHolder.navigateTo(MainNavigationRoute.Login)
                },
                onSignUp = { email, password ->
                    authenticationViewModel.loginWithEmail(email, password)
                }
            )
        }

        composable(MainNavigationRoute.VerificationCode.name) {
            VerificationCodeScreen(
                authenticationViewModel = authenticationViewModel
            )
        }

    }
}