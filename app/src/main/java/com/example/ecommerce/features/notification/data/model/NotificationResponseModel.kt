package com.example.ecommerce.features.notification.data.model

import com.google.gson.annotations.SerializedName

data class NotificationResponseModel(
    @SerializedName("order_id") val userId: String,
    @SerializedName("status") val status: String,
)