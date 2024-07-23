package com.example.authentication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.authentication.stateholder.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationActivity: ComponentActivity() {

    private val authenticationViewModel by viewModel<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveData()

        setContent {
            MaterialTheme {
                AuthenticationNavigationHost(
                    authenticationViewModel = authenticationViewModel,
                    onConfirmSignUp = { email, password ->
                        authenticationViewModel.registerWithEmail(email, password)
                    },
                    onFinish = {
                        finish()
                    }
                )
                if (authenticationViewModel.isLoading.value) {
                    //LoadingDialog()
                }
            }
        }

    }

    private fun observeLiveData() {
        authenticationViewModel.isLoginSuccess.observe(this) {
            goToMainActivity()
        }
        authenticationViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
    }

}