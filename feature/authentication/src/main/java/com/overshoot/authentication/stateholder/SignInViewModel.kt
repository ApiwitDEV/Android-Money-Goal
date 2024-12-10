package com.overshoot.authentication.stateholder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.repository.AuthenticationRepository
import com.overshoot.domain.usecase.authentication.LoginWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RequestVerificationCodeUseCase
import com.overshoot.domain.usecase.authentication.VerifyCodeUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase,
    private val requestVerificationCodeUseCase: RequestVerificationCodeUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {

    private val _isLoginSuccess = MutableLiveData<Unit>()
    val isLoginSuccess: LiveData<Unit> = _isLoginSuccess

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _countDown = mutableLongStateOf(-1)
    val countDown: State<Long> = _countDown

    fun isSigned(): Boolean {
        return authenticationRepository.isSignedCheck()
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

    fun requestVerificationCode() {
        viewModelScope.launch {
            _isLoading.value = true
            requestVerificationCodeUseCase.invoke()
                .collect {
                    it
                        .onSuccess { timeout ->
                            launch {
                                _isLoading.value = false
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

    fun signOut() {
        authenticationRepository.logout()
    }

}