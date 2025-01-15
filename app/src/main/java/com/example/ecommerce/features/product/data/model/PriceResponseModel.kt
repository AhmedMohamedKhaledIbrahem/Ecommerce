package com.example.ecommerce.features.product.data.model

import com.google.gson.annotations.SerializedName

data class PriceResponseModel(
    @SerializedName("price") val price: String,
)


