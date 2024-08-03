package com.overshoot.data.datasource.remote.model.something

import com.squareup.moshi.Json

data class RequestBody(
    @field:Json(name = "data")
    val data: String
)
