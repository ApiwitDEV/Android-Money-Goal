package com.overshoot.moneygoal.component.home.uistatemodel

data class GoalItemUIState(
    val id: Int,
    val name: String,
    val isSuccess: Boolean,
    val target: Double,
    val objective: String
)