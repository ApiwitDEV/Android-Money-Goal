package com.overshoot.data.datasource.remote.model.categories

import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
