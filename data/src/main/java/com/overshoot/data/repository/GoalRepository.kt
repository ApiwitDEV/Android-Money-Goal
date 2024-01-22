package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.goal.GoalEntity

interface GoalRepository {

    suspend fun addGoal(): ResultData<Int>

    suspend fun getGoal(): ResultData<GoalEntity>

    suspend fun editGoal(): ResultData<GoalEntity>

    suspend fun deleteGoal(): ResultData<GoalEntity>

}