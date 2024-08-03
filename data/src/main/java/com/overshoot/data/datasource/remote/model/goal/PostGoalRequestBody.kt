package com.overshoot.data.datasource.remote.model.goal

import com.squareup.moshi.Json

data class PostGoalRequestBody(
    @field:Json(name = "id")
    val id: Int = 0,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "create_at")
    val createAt: String?,
    @field:Json(name = "update_at")
    val updateAt: String?,
    @field:Json(name = "objective")
    val objective: String?,
    @field:Json(name = "period")
    val period: String?,
    @field:Json(name = "target")
    val target: Double?,
    @field:Json(name = "cost")
    val cost: Double?,
    @field:Json(name = "income")
    val income: Double?,
)
