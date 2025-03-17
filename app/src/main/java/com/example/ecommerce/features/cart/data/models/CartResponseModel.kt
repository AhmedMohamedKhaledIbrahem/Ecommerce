package com.example.ecommerce.features.cart.data.models

import com.google.gson.annotations.SerializedName

data class CartResponseModel(
    @SerializedName("cart_hash") val cartHash: String,
    @SerializedName("cart_key") val cartKey: String,
    @SerializedName("items") val items: List<CartItemResponseModel>

)

