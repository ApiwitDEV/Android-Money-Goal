package com.overshoot.moneygoal.component.account.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.domain.usecase.authentication.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    fun logout() {
        viewModelScope.launch(Dispatchers.Default) {
            logoutUseCase.invoke()
        }
    }

}