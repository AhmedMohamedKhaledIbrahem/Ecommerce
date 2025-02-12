package com.example.ecommerce.features.orders.domain.entities

import com.google.gson.annotations.SerializedName

data class LineItemRequestEntity(
    @SerializedName("product_id") val productId:Int,
    @SerializedName("quantity") val quantity:Int
)