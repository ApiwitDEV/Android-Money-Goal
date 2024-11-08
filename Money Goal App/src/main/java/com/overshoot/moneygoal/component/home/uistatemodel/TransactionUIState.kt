package com.overshoot.moneygoal.component.home.uistatemodel

data class TransactionUIState(
    val name: String?,
    val type: String?,
    val value: Double?,
    val remark: String?,
    val goalId: Int?,
    val category: String?,
    val isLoading: Boolean?
)