package com.overshoot.data.datasource.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserInfo(user: UserInfoEntity)

    @Query("SELECT * FROM USER_INFO_TABLE")
    fun getUserInfo(): List<UserInfoEntity>

}