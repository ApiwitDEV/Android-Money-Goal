package com.overshoot.moneygoal.component.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AccountContent(
    signOut: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Text(text = "Account")
            Button(onClick = signOut) {
                Text(text = "Sign out")
            }
        }
    }
}