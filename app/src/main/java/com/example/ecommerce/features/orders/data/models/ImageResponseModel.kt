package com.example.ecommerce.features.orders.data.models

import com.google.gson.annotations.SerializedName

data class ImageResponseModel(
    @SerializedName("src") val imagePath: String
)