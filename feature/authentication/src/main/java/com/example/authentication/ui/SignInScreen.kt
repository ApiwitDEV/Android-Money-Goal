package com.example.authentication.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.authentication.AuthenticationRoute
import com.example.authentication.stateholder.AuthenticationViewModel

@Composable
fun SignInScreen(
    authenticationViewModel: AuthenticationViewModel,
    onNavigateBack: () -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    BackHandler {
        onNavigateBack()
    }
    LoginContent(
        email = email,
        password = password,
        onSignUpClicked = {
            authenticationViewModel.navigateTo(AuthenticationRoute.Register)
        },
        onSignInClicked = {
            authenticationViewModel.loginWithEmail(email, password)
        },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onRequestVerificationCode = {
            authenticationViewModel.requestVerificationCode()
        }
    )
}

@Composable
private fun LoginContent(
    email: String,
    password: String,
    onSignUpClicked: () -> Unit = { },
    onSignInClicked: () -> Unit = { },
    onEmailChange: (String) -> Unit = {_ ->},
    onPasswordChange: (String) -> Unit = {_ ->},
    onRequestVerificationCode: () -> Unit = { }
) {
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
                value = email,
                singleLine = true,
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                placeholder = {
                    Text(text = "email")
                },
                supportingText = {
                },
                onValueChange = onEmailChange
            )
            OutlinedTextField(
                value = password,
                singleLine = true,
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text(text = "password")
                },
                onValueChange = onPasswordChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = { onSignUpClicked() }) {
                    Text(
                        text = "Sign up",
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    onClick = onSignInClicked
                ) {
                    Text(
                        text = "Sign in",
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {  }
            ) {
                Text(
                    text = "Sign in with Google",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRequestVerificationCode
            ) {
                Text(
                    text = "Sign in with Phone",
                    fontSize = 16.sp
                )
            }
        }
    }
}

//@Composable
//@Preview(device = Devices.PIXEL_4)
//private fun LoginContentPreview() {
//    MoneyGoalTheme {
//        LoginContent(email = "", password = "")
//    }
//}