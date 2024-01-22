package com.example.moneygoal.component.home.uistatemodel

data class TransactionUIState(
    val id: Int,
    val label: String?,
    val cost: Double?,
    val remark: String?,
    val goalId: Int?
)