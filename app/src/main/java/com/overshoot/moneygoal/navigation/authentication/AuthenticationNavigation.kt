package com.overshoot.moneygoal.navigation.authentication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.overshoot.moneygoal.component.authentication.ui.SignInScreen
import com.overshoot.moneygoal.component.authentication.ui.SignUpScreen

@Composable
fun AuthenticationNavigationHost(
    onConfirmSignIn: (String, String) -> Unit,
    onConfirmSignUp: (String, String) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AuthenticationRoute.Login.name) {
        composable(AuthenticationRoute.Login.name) {
            SignInScreen(
                onSignUpClicked = {
                    navController.navigate(AuthenticationRoute.Register.name)
                },
                onSignIn = { email, password ->
                    onConfirmSignIn(email, password)
                }
            )
        }
        composable(AuthenticationRoute.Register.name) {
            SignUpScreen { email, password ->
                onConfirmSignUp(email, password)
            }
        }
    }
}