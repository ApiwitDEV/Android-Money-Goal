package com.overshoot.moneygoalapp.component.home.upstreamdatamodel

data class AddTransactionData(
    val name: String,
    val categoryId: String,
    val remark: String,
    val type: String,
    val value: Double
)
