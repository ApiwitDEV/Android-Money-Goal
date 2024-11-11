package com.overshoot.moneygoal.navigation.authentication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authentication.stateholder.SignInViewModel
import com.example.authentication.ui.SignInScreen
import com.example.authentication.ui.SignUpScreen
import com.example.authentication.ui.VerificationCodeScreen
import com.overshoot.moneygoal.AppStateHolder
import com.overshoot.moneygoal.component.dashboard.DashboardScreen
import com.overshoot.moneygoal.component.home.ui.MainScreen

@Composable
fun AuthenticationNavigationHost(
    appStateHolder: AppStateHolder,
    signInViewModel: SignInViewModel,
    onCloseApp: () -> Unit,
    xxx: () -> Unit
) {
    val navController = appStateHolder.authenticationNavController

    NavHost(
        navController = navController,
        startDestination = if (signInViewModel.isSigned()) AuthenticationNavigationRoute.Main.name else AuthenticationNavigationRoute.Login.name
    ) {

        composable(route = AuthenticationNavigationRoute.Main.name) {
            MainScreen(
                appStateHolder = appStateHolder,
                onBackPressed = {
                    onCloseApp()
                }
            )
        }

        composable(AuthenticationNavigationRoute.Login.name) {
            SignInScreen(
                signInViewModel = signInViewModel,
                onSignUpClicked = {
                    appStateHolder.clearBackStackAndNavigateTo(AuthenticationNavigationRoute.Register)
                },
                onNavigateBack = onCloseApp,
                xxx = xxx
            )
        }

        composable(AuthenticationNavigationRoute.Register.name) {
            SignUpScreen(
                onBack = {
                    appStateHolder.clearBackStackAndNavigateTo(AuthenticationNavigationRoute.Login)
                },
            )
        }

        composable(AuthenticationNavigationRoute.VerificationCode.name) {
            VerificationCodeScreen(
                signInViewModel = signInViewModel
            )
        }

        composable(AuthenticationNavigationRoute.Dashboard.name) {
            DashboardScreen()
        }

    }

}