package com.overshoot.data.datasource.remote.model.employees

import com.squareup.moshi.Json

data class Data(
    @field:Json(name = "age")
    val age: Int?,
    @field:Json(name = "name")
    val name: String?
)