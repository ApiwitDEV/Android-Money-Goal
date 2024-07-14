package com.overshoot.moneygoal.component.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.overshoot.moneygoal.MainActivity
import com.overshoot.moneygoal.common.ui.LoadingDialog
import com.overshoot.moneygoal.component.authentication.stateholder.SignInSignUpViewModel
import com.overshoot.moneygoal.navigation.authentication.AuthenticationNavigationHost
import com.overshoot.moneygoal.theme.MoneyGoalTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationActivity: ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val signInSignUpViewModel by viewModel<SignInSignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeLiveData()
        auth = Firebase.auth

        setContent {
            MoneyGoalTheme {
                AuthenticationNavigationHost(
                    onConfirmSignIn = { email, password ->
                        signInSignUpViewModel.loginWithEmail(email, password)
                    },
                    onConfirmSignUp = { email, password ->
                        signInSignUpViewModel.registerWithEmail(email, password)
                    }
                )
                if (signInSignUpViewModel.isLoading.value) {
                    LoadingDialog()
                }
            }
        }

    }

    private fun observeLiveData() {
        signInSignUpViewModel.goToMainActivity.observe(this) {
            goToMainActivity()
        }
        signInSignUpViewModel.failure.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}