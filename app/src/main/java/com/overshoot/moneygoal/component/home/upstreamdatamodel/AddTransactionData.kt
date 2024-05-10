package com.overshoot.moneygoal.component.home.upstreamdatamodel

data class AddTransactionData(
    val name: String,
    val categoryId: Int,
    val remark: String,
    val type: String,
    val value: Double
)
