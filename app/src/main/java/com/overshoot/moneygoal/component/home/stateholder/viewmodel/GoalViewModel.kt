package com.overshoot.moneygoal.component.home.stateholder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.domain.GetGoalUseCase
import com.overshoot.domain.AddGoalUseCase
import kotlinx.coroutines.launch

class GoalViewModel(
    private val getGoalUseCase: GetGoalUseCase,
    private val addGoalUseCase: AddGoalUseCase
): ViewModel() {

    init {
        println()
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _x = mutableStateOf(0)
    val x: State<Int> = _x

    fun addGoal() {
        viewModelScope.launch {
            addGoalUseCase.addGoal()
                .onSuccess {
                    _x.value = it
                }
                .onFailure {
                    _error.value = it
                }
        }
    }

}