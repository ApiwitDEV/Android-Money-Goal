package com.overshoot.data.datasource.remote.model.receipt


import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("discount")
    val discount: Double?,
    @SerializedName("products")
    val products: List<Product?>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("total")
    val total: Double?
)