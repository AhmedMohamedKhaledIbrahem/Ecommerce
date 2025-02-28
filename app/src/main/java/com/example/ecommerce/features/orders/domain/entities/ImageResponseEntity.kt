package com.example.ecommerce.features.orders.domain.entities

import com.google.gson.annotations.SerializedName

data class ImageResponseEntity(
    @SerializedName("src") val imagePath: String
)