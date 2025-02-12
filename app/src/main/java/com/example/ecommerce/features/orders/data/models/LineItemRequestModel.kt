package com.example.ecommerce.features.orders.data.models

import com.google.gson.annotations.SerializedName

data class LineItemRequestModel(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int
)
