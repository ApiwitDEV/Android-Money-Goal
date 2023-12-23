package com.example.domain

import com.example.data.datasource.ResultData
import com.example.data.repository.GoalRepository

class AddGoalUseCase(private val currentGoalRepository: GoalRepository) {

    suspend fun addGoal(): ResultData<Int> {
        return currentGoalRepository.addGoal()
    }

}