package com.overshoot.data.datasource.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info_table")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "key")
    val key: Int = 0,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String,
    @ColumnInfo(name = "user_token")
    val userName: String
)
