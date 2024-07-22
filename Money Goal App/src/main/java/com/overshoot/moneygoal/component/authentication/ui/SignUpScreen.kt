package com.overshoot.moneygoal.component.authentication.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onSignUp: (String, String) -> Unit
) {
    BackHandler(onBack = onBack)
    Button(onClick = { onSignUp("username", "password") }) {
        Text(text = "Sign Up")
    }
}