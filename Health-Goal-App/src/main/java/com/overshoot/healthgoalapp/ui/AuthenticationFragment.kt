package com.overshoot.healthgoalapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import com.overshoot.feature.authentication.stateholder.SignInViewModel
import com.overshoot.feature.authentication.ui.SignInScreen
import com.overshoot.data.repository.AuthenticationRepository
import com.overshoot.healthgoalapp.databinding.FragmentAuthenticationBinding
import com.overshoot.healthgoalapp.ui.theme.HealthGoalTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationFragment: Fragment() {

    private lateinit var _binding: FragmentAuthenticationBinding

    private val signInViewModel by viewModel<SignInViewModel>()
    private val authenticationRepository by inject<AuthenticationRepository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        _binding.composeView.apply {
            setContent {
                HealthGoalTheme {
                    LaunchedEffect(key1 = Unit) {
                        signInViewModel.isLoginSuccess.asFlow().collect {
                            Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if (signInViewModel.isLoading.value) {
                        LoadingDialog()
                    }
                    if (authenticationRepository.isSignedCheck()) {
                        Text("BAS")
                    }
                    else {
                        SignInScreen(
                            signInViewModel = signInViewModel,
                            onNavigateBack = {},
                            onSignUpClicked = {},
                            xxx = {}
                        )
                    }
                }
            }
        }
        return  _binding.root
    }

    @Composable
    fun LoadingDialog() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(80.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Loading")
            }
        }
    }

}