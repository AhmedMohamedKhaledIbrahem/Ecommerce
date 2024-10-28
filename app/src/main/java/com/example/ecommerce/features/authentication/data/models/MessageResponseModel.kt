package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class MessageResponseModel(
    @SerializedName("message")
    val message:String
)
