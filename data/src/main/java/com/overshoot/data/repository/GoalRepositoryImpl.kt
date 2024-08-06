package com.overshoot.data.repository

import android.icu.util.Calendar
import android.util.Log
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.local.goal.GoalDao
import com.overshoot.data.datasource.local.goal.GoalEntity
import com.overshoot.data.datasource.onFailure
import com.overshoot.data.datasource.onSuccess
import com.overshoot.data.datasource.remote.MoneyGoalApiService
import com.overshoot.data.datasource.remote.model.goal.PostGoalRequestBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.await
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

class GoalRepositoryImpl(
    private val goalDao: GoalDao,
    private val moneyGoalApiService: MoneyGoalApiService
): BaseRepository(), GoalRepository {

    override suspend fun addGoal(
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

    override suspend fun getGoal(): Flow<List<GoalEntity>> {
        return goalDao.getGoal()
    }

    override suspend fun editGoal(): ResultData<GoalEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGoal(): ResultData<GoalEntity> {
        TODO("Not yet implemented")
    }

}