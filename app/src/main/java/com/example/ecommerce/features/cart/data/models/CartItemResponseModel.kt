package com.example.ecommerce.features.cart.data.models

import com.google.gson.annotations.SerializedName

data class CartItemResponseModel(
    @SerializedName("item_key") val itemKey: String,
    val id: Int,
    val name: String,
    val price: String,
    @SerializedName("quantity") val quantity: QuantityResponseModel,
    @SerializedName("featured_image") val imageItemLink: String
)
