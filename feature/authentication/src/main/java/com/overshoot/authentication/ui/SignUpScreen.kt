package com.overshoot.authentication.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.overshoot.authentication.stateholder.SignUpViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    signUpViewModel: SignUpViewModel = koinViewModel()
) {
    BackHandler(onBack = onBack)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val errorMessage = signUpViewModel.errorMessage
    if (signUpViewModel.signUpSuccess.value) {
        onBack()
    }
    SignUpContent(
        isLoading = signUpViewModel.isLoading.value,
        isEmailError = signUpViewModel.emailError.value.isNotEmpty(),
        isPasswordError = signUpViewModel.passwordError.value.isNotEmpty(),
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        emailError = signUpViewModel.emailError.value,
        passwordError = signUpViewModel.passwordError.value,
        errorMessage = errorMessage.value,
        onSignUp = {
            signUpViewModel.registerWithEmail(email, password, confirmPassword)
        },
        onEmailChange = { enterEmail ->
            email = enterEmail
        },
        onPasswordChange = { enterPassword ->
            password = enterPassword
        },
        onConfirmPasswordChange = { enterPassword ->
            confirmPassword = enterPassword
        }
    )
}

@Composable
private fun SignUpContent(
    isLoading: Boolean,
    isEmailError: Boolean,
    isPasswordError: Boolean,
    email: String,
    password: String,
    confirmPassword: String,
    emailError: String,
    passwordError: String,
    errorMessage: String,
    onSignUp: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    Scaffold(modifier = Modifier.imePadding()) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                value = email,
                isError = isEmailError,
                singleLine = true,
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                placeholder = {
                    Text(text = "email")
                },
                onValueChange = onEmailChange
            )
            Text(text = emailError)
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                isError = isPasswordError,
                value = password,
                singleLine = true,
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text(text = "password")
                },
                onValueChange = onPasswordChange
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                value = confirmPassword,
                isError = isPasswordError,
                singleLine = true,
                shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = {
                    Text(text = "Confirm Password")
                },
                onValueChange = onConfirmPasswordChange
            )
            Text(text = errorMessage.ifEmpty { passwordError })
            Button(
                modifier = Modifier.fillMaxWidth(0.75f),
                onClick = onSignUp
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}