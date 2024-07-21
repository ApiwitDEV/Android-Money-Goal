package com.overshoot.domain.usecase.goal

import com.overshoot.data.datasource.local.goal.GoalEntity
import com.overshoot.data.repository.GoalRepository
import kotlinx.coroutines.flow.Flow

class GetGoalUseCase(private val currentGoalRepository: GoalRepository) {

    suspend fun getAllGoal(): Flow<List<GoalEntity>> {
//        delay(3000)
//        return Success(
//            data = mutableListOf<GoalEntity>().apply {
//                repeat(100) {
//                    add(GoalEntity(
//                        id = it,
//                        name = it.toString(),
//                        description = null,
//                        target = it*10.0,
//                        createAt = "",
//                        updateAt = "",
//                        cost = 0.0,
//                        income = 0.0,
//                        objective = "",
//                        period = "",
//                        supportTo = listOf(),
//                        transactionIds = listOf()
//                    ))
//                }
//            }
//        )
        return currentGoalRepository.getGoal()
    }

}