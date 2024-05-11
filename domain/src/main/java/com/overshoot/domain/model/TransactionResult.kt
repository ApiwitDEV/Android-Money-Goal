package com.overshoot.domain.model

data class TransactionResult(
    val id: Int,
    val name: String?,
    val type: String?,
    val value: Double?,
    val remark: String?,
    val goalId: Int?,
    val categoryId: Int,
    val categoryName: String?
)