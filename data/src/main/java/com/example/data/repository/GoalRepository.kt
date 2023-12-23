package com.example.data.repository

import com.example.data.datasource.ResultData
import com.example.data.datasource.local.goal.GoalEntity

interface GoalRepository {

    suspend fun addGoal(): ResultData<Int>

    suspend fun getGoal(): ResultData<GoalEntity>

    suspend fun editGoal(): ResultData<GoalEntity>

    suspend fun deleteGoal(): ResultData<GoalEntity>

}