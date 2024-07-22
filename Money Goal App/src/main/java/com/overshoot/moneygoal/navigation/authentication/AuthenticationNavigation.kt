package com.overshoot.moneygoal.navigation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.overshoot.moneygoal.component.authentication.stateholder.AuthenticationViewModel
import com.overshoot.moneygoal.component.authentication.ui.SignInScreen
import com.overshoot.moneygoal.component.authentication.ui.SignUpScreen
import com.overshoot.moneygoal.component.authentication.ui.VerificationCodeScreen

@Composable
fun AuthenticationNavigationHost(
    authenticationViewModel: AuthenticationViewModel,
    onConfirmSignUp: (String, String) -> Unit,
    onFinish: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = remember { authenticationViewModel.authenticationCurrentScreen.value.name }
    ) {
        composable(AuthenticationRoute.Login.name) {
            SignInScreen(
                authenticationViewModel = authenticationViewModel,
                onNavigateBack = {
                    onFinish()
                }
            )
        }
        composable(AuthenticationRoute.Register.name) {
            SignUpScreen(
                onBack = {
                    authenticationViewModel.navigateTo(AuthenticationRoute.Login)
                }
            ) { email, password ->
                onConfirmSignUp(email, password)
            }
        }
        composable(AuthenticationRoute.VerificationCode.name) {
            VerificationCodeScreen(authenticationViewModel = authenticationViewModel)
        }
    }

    LaunchedEffect(Unit) {
        authenticationViewModel.authenticationCurrentScreen.collect {
            if (navController.currentDestination?.route != it.name) {
                navController.navigate(
                    it.name,
                    navOptions {
                        popUpTo(navController.graph.startDestinationId)
                    }
                )
            }
        }
    }
}