package com.overshoot.moneygoal.component.authentication.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.overshoot.moneygoal.component.authentication.stateholder.AuthenticationViewModel
import com.overshoot.moneygoal.navigation.authentication.AuthenticationRoute

@Composable
fun VerificationCodeScreen(
    authenticationViewModel: AuthenticationViewModel
) {
    var code by remember { mutableStateOf("") }
    BackHandler {
        authenticationViewModel.navigateTo(AuthenticationRoute.Login)
    }
    Scaffold {
        Column(
            modifier= Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(it.calculateBottomPadding()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = code,
                singleLine = true,
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                placeholder = {
                    Text(text = "Verification Code")
                },
                supportingText = {
                },
                onValueChange = { c ->
                    if (c.length <= 6) {
                        code = c
                    }
                    if (c.length == 6) {
                        authenticationViewModel.verifyCode(code)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "remaining time ${authenticationViewModel.countDown.value.takeIf { value -> value >= 0 }}")
            Text(text = "Resend Code")
        }
    }
}