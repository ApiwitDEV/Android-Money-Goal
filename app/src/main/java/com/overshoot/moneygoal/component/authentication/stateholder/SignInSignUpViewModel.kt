package com.overshoot.moneygoal.component.authentication.stateholder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.domain.usecase.authentication.LoginWithEmailUseCase
import com.overshoot.domain.usecase.authentication.RegisterWithEmailUseCase
import kotlinx.coroutines.launch

class SignInSignUpViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val registerWithEmailUseCase: RegisterWithEmailUseCase
): ViewModel() {

    private val _goToMainActivity = MutableLiveData<Unit>()
    val goToMainActivity: LiveData<Unit> = _goToMainActivity

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String> = _failure

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            loginWithEmailUseCase.invoke(email, password)
                .collect {
                    it
                        .onSuccess {
                            _isLoading.value = false
                            _goToMainActivity.value = Unit
                        }
                        .onFailure {
                            _isLoading.value = false
                            _failure.value = it
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
                            _goToMainActivity.value = Unit
                        }
                        .onFailure {
                            _isLoading.value = false
                            _failure.value = it
                        }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}