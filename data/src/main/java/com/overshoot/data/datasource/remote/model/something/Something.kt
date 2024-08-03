package com.overshoot.data.datasource.remote.model.something

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Something(
    @field:Json(name = "data")
    val data: String,
    @field:Json(name = "message")
    val message: String
)
