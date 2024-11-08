package com.overshoot.data.datasource.remote.model.transaction

import com.squareup.moshi.Json

data class TransactionData(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "create_at")
    val createAt: String,
    @field:Json(name = "update_at")
    val updateAt: String,
    @field:Json(name = "category_id")
    val categoryId: String,
    @field:Json(name = "money_amount")
    val moneyAmount: Double,
    @field:Json(name = "remark")
    val remark: String?,
    @field:Json(name = "type")
    val type: String
)
