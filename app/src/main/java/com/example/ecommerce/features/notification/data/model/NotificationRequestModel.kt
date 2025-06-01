package com.example.ecommerce.features.notification.data.model

import com.google.gson.annotations.SerializedName

data class NotificationRequestModel(
    @SerializedName("token") val token: String,
)

data class NotificationResponseModel(
    @SerializedName("status") val message: String,
)