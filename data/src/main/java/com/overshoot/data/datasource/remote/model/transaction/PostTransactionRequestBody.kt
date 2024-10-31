package com.overshoot.data.datasource.remote.model.transaction

import com.squareup.moshi.Json

data class PostTransactionRequestBody(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "categoryId")
    val categoryId: Int,
    @field:Json(name = "remark")
    val remark: String,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "value")
    val value: Double
)
