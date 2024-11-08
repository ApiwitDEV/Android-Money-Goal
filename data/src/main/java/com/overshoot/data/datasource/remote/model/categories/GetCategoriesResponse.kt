package com.overshoot.data.datasource.remote.model.categories

import com.google.gson.annotations.SerializedName

data class GetCategoriesResponse(
    @SerializedName("categories")
    val categories: List<CategoryData>,
    @SerializedName("message")
    val message: String
)
