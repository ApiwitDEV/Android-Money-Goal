package com.overshoot.data.datasource.remote.model.receipt


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("quantity")
    val quantity: Int?
)