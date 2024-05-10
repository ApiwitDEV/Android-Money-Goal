package com.overshoot.moneygoal.component.home.uistatemodel

data class TransactionUIState(
    val id: Int,
    val name: String?,
    val type: String?,
    val value: Double?,
    val remark: String?,
    val goalId: Int?
)