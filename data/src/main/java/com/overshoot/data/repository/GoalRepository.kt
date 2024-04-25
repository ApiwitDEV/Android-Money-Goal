package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.goal.GoalEntity
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    suspend fun addGoal(): ResultData<Unit>

    suspend fun getGoal(): Flow<List<GoalEntity>>

    suspend fun editGoal(): ResultData<GoalEntity>

    suspend fun deleteGoal(): ResultData<GoalEntity>

}