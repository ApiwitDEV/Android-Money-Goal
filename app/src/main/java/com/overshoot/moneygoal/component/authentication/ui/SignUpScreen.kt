package com.overshoot.moneygoal.component.authentication.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SignUpScreen(onSignUp: (String, String) -> Unit) {
    Button(onClick = { onSignUp("username", "password") }) {
        Text(text = "Sign Up")
    }
}