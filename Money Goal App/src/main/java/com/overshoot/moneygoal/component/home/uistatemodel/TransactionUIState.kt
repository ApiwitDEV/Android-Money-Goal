package com.overshoot.moneygoal.component.home.uistatemodel

data class TransactionUIState(
    val id: String,
    val name: String?,
    val type: String?,
    val value: Double?,
    val remark: String?,
    val goalId: Int?,
    val category: String?,
    val isSelected: Boolean,
    val isEdit: Boolean
)