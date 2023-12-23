package com.example.data.datasource.local.transaction

data class TransactionEntity(
    val id: Int,
    val label: String?,
    val cost: Double?,
    val remark: String?,
    val goalId: Int?
)