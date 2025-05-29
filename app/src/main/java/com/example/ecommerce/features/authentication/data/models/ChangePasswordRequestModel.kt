package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequestModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("password")
    val password: String
)