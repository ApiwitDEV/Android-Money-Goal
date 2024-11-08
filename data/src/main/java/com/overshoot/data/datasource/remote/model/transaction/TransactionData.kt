package com.overshoot.data.datasource.remote.model.transaction

import com.google.gson.annotations.SerializedName

data class TransactionData(
    val id: String,
    val name: String,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("money_amount")
    val moneyAmount: Double,
    val remark: String?,
    val type: String
)
