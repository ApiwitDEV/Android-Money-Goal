package com.overshoot.domain.model

data class TransactionResult(
    val id: String?,
    val name: String?,
    val type: String?,
    val moneyAmount: Double?,
    val remark: String?,
    val goalId: Int?,
    val categoryId: String?,
    val categoryName: String?
)