package com.example.moneygoal.viewmodel

import androidx.lifecycle.ViewModel
import com.example.domain.GetCurrentGoalUseCase

class CurrentGoalViewModel(
    private val getCurrentGoalUseCase: GetCurrentGoalUseCase
): ViewModel() {
}