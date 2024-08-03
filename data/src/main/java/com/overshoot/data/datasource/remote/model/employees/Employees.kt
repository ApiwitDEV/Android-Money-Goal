package com.overshoot.data.datasource.remote.model.employees

import com.squareup.moshi.Json

data class Employees(
    @field:Json(name = "data")
    val data: List<Data?>?,
    @field:Json(name = "message")
    val message: String?
)