package com.overshoot.data.repository

import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import com.overshoot.data.datasource.local.goal.GoalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GoalRepositoryImpl: BaseRepository(), GoalRepository {

    override suspend fun addGoal(): ResultData<Int> {
        return callDB {
            var x = 0
            coroutineScope {
                launch {
                    repeat(1) {
                        delay(1000)
                        x = it + 1
                    }
                }
                launch {
                    repeat(5) {
                        delay(1000)
                        x = it + 1
                    }
                }
            }
            x
        }
    }

    override suspend fun getGoal(): ResultData<GoalEntity> {
        val data = CoroutineScope(Dispatchers.Default)
            .async {
                GoalEntity(
                    id = 1,
                    name = "aa",
                    description = "bb",
                    budget = 500.0
                )
            }
            .await()
        return Success(data)
    }

    override suspend fun editGoal(): ResultData<GoalEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGoal(): ResultData<GoalEntity> {
        TODO("Not yet implemented")
    }

}