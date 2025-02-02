package com.overshoot.data.datasource.local.goal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGoal(goalEntity: GoalEntity)

    @Query("SELECT * FROM GOAL_TABLE")
    fun getGoal(): Flow<List<GoalEntity>>

    @Query("DELETE FROM GOAL_TABLE")
    suspend fun deleteAll()

}