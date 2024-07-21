package com.overshoot.domain.usecase.goal

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.repository.GoalRepository

class AddGoalUseCase(private val currentGoalRepository: GoalRepository) {

    suspend fun addGoal(
        goalName: String,
        goalObjective: String,
        period: String,
        targetValue: Double
    ): ResultData<Unit> {
        return currentGoalRepository.addGoal(
            goalName = goalName,
            goalObjective = goalObjective,
            period = period,
            targetValue = targetValue
        )
    }

}