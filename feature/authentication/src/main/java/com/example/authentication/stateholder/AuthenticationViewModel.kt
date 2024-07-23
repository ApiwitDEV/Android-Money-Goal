package com.example.authentication.stateholder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.AuthenticationRoute
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.domain.usecase.authentication.LoginWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RequestVerificationCodeUseCase
import com.overshoot.domain.usecase.authentication.VerifyCodeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase,
    private val requestVerificationCodeUseCase: RequestVerificationCodeUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase
): ViewModel() {

    private val _isLoginSuccess = MutableLiveData<Unit>()
    val isLoginSuccess: LiveData<Unit> = _isLoginSuccess

    private val _authenticationCurrentScreen = MutableStateFlow(AuthenticationRoute.Login)
    val authenticationCurrentScreen: StateFlow<AuthenticationRoute> = _authenticationCurrentScreen

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _countDown = mutableLongStateOf(-1)
    val countDown: State<Long> = _countDown

    fun navigateTo(page: AuthenticationRoute) {
        _authenticationCurrentScreen.value = page
    }

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            loginWithEmailUseCase.invoke(email, password)
                .collect {
                    it
                        .onSuccess {
                            _isLoading.value = false
                            _isLoginSuccess.value = Unit
                        }
                        .onFailure { message ->
                            _isLoading.value = false
                            _errorMessage.value = message
                        }
                }
        }
    }

    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            registerWithEmailUseCase.invoke("", "", email, password)
                .collect {
                    it
                        .onSuccess {
                            _isLoading.value = false
                            _isLoginSuccess.value = Unit
                        }
                        .onFailure { message ->
                            _isLoading.value = false
                            _errorMessage.value = message
                        }
                }
        }
    }

    fun requestVerificationCode() {
        viewModelScope.launch {
            _isLoading.value = true
            requestVerificationCodeUseCase.invoke()
                .collect {
                    it
                        .onSuccess { timeout ->
                            launch {
                                _isLoading.value = false
                                _authenticationCurrentScreen.value = AuthenticationRoute.VerificationCode
                            }
                            launch {
                                var timer = timeout
                                while (timer >= 0) {
                                    _countDown.longValue = timer--
                                    delay(1000)
                                }
                            }
                        }
                        .onFailure { message ->
                            _isLoading.value = false
                            _errorMessage.value = message
                        }
                }
        }
    }

    fun verifyCode(code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            verifyCodeUseCase.invoke(code = code)
                .collect {
                    it
                        .onSuccess {
                            _isLoading.value = false
                            _isLoginSuccess.value = Unit
                        }
                        .onFailure { message ->
                            _isLoading.value = false
                            _errorMessage.value = message
                        }
                }
        }
    }

}