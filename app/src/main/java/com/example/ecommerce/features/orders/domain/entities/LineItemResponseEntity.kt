package com.example.ecommerce.features.orders.domain.entities

import com.google.gson.annotations.SerializedName

data class LineItemResponseEntity(
    @SerializedName("id") val lineItemId: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("name") val itemName: String,
    @SerializedName("total") val totalPrice: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("image") val image: ImageResponseEntity
)
