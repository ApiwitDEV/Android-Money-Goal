package com.overshoot.data.datasource.remote.model.categories

data class GetCategoriesResponse(
    val categories: List<CategoryData>,
    val message: String
)
