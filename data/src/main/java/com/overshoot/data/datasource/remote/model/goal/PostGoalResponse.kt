package com.overshoot.data.datasource.remote.model.goal

import com.squareup.moshi.Json

data class PostGoalResponse(
    @field:Json(name = "data")
    val data: PostGoalRequestBody?,
    @field:Json(name = "message")
    val message: String
)
