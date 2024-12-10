package com.overshoot.authentication.ui

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
import com.overshoot.authentication.stateholder.SignInViewModel

@Composable
fun VerificationCodeScreen(
    signInViewModel: SignInViewModel
) {
    var code by remember { mutableStateOf("") }
    BackHandler {

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
                        signInViewModel.verifyCode(code)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "remaining time ${signInViewModel.countDown.value.takeIf { value -> value >= 0 }}")
            Text(text = "Resend Code")
        }
    }
}