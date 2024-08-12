package com.example.authentication.stateholder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val registerWithEmailUseCase: RegisterWithEmailUseCase,
): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _signUpSuccess = mutableStateOf(false)
    val signUpSuccess: State<Boolean> = _signUpSuccess

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _emailError = mutableStateOf("")
    val emailError: State<String> = _emailError

    private val _passwordError = mutableStateOf("")
    val passwordError: State<String> = _passwordError

    fun registerWithEmail(email: String, password: String, confirmPassword: String) {
        _emailError.value = ""
        _passwordError.value = ""
        _errorMessage.value = ""
        if (email.isEmpty()) {
            _emailError.value = "กรุณากรอก email"
            return
        }
        if (password.isEmpty() && confirmPassword.isEmpty()) {
            _passwordError.value = "กรุณากรอก password"
            return
        }
        if (password != confirmPassword) {
            _passwordError.value = "password ไม่เหมือนกัน"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            registerWithEmailUseCase.invoke("", "", email, password)
                .collect {
                    it
                        .onSuccess {
                            _errorMessage.value = ""
                            _isLoading.value = false
                            _signUpSuccess.value = true
                        }
                        .onFailure { message ->
                            _isLoading.value = false
                            _errorMessage.value = message
                        }
                }
        }
    }

}