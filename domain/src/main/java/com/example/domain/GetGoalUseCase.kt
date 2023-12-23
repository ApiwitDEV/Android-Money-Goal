package com.example.domain

import com.example.data.repository.GoalRepository
import com.example.data.repository.GoalRepositoryImpl

class GetGoalUseCase(private val currentGoalRepository: GoalRepository) {
}