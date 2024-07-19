package com.overshoot.moneygoal.component.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.overshoot.moneygoal.MainActivity
import com.overshoot.moneygoal.common.ui.LoadingDialog
import com.overshoot.moneygoal.component.authentication.stateholder.AuthenticationViewModel
import com.overshoot.moneygoal.navigation.authentication.AuthenticationNavigationHost
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationActivity: ComponentActivity() {

    private val authenticationViewModel by viewModel<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeLiveData()

        setContent {
            MoneyGoalTheme {
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
                    LoadingDialog()
                }
            }
        }

    }

    private fun observeLiveData() {
        authenticationViewModel.goToMainActivity.observe(this) {
            goToMainActivity()
        }
        authenticationViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}