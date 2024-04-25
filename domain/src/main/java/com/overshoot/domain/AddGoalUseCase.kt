package com.overshoot.domain

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.repository.GoalRepository

class AddGoalUseCase(private val currentGoalRepository: GoalRepository) {

    suspend fun addGoal(): ResultData<Unit> {
        return currentGoalRepository.addGoal()
    }

}