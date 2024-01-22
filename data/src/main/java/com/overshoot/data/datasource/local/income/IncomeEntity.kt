package com.overshoot.data.datasource.local.income

import java.util.Date

data class IncomeEntity(
    val id: Int,
    val income: Double?,
    val remark: String?,
    val dateTime: Date
)