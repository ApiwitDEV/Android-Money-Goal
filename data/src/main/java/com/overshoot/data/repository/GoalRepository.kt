package com.overshoot.data.repository

import android.icu.util.Calendar
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.goal.GoalDao
import com.overshoot.data.datasource.local.goal.GoalEntity
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.datasource.remote.MoneyGoalApiService
import com.overshoot.data.datasource.remote.model.goal.PostGoalRequestBody
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat

class GoalRepository(
    private val goalDao: GoalDao,
    private val moneyGoalApiService: MoneyGoalApiService
): BaseRepository() {

    suspend fun addGoal(
        goalName: String,
        goalObjective: String,
        period: String,
        targetValue: Double
    ): ResultData<Unit> {
        val time = Calendar.getInstance().time
        callRestApi {
//            moneyGoalApiService.getUnauthorizedTest().execute()
            moneyGoalApiService.postGoal(
                PostGoalRequestBody(
                    name = goalName,
                    description = "",
                    createAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                    updateAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                    objective = goalObjective,
                    period = period,
                    target = targetValue,
                    cost = 10.0,
                    income = 20.0
                )
            )
        }
            .onFailure {
                println(it)
            }
            .onSuccess {
                println(it)
            }
        return callDB {
            val goal = GoalEntity(
                name = goalName,
                description = "",
                createAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                updateAt = SimpleDateFormat("yyyy-MM-dd HH:mm").format(time),
                objective = goalObjective,
                period = period,
                target = targetValue,
                cost = 10.0,
                income = 20.0
            )
            goalDao.addGoal(goal)
        }
    }

    suspend fun getGoal(): Flow<List<GoalEntity>> {
        return goalDao.getGoal()
    }

    suspend fun editGoal(): ResultData<GoalEntity> {
        TODO("Not yet implemented")
    }

    suspend fun deleteGoal(): ResultData<GoalEntity> {
        TODO("Not yet implemented")
    }

    suspend fun deleteAllGoal() {
        return goalDao.deleteAll()
    }

}