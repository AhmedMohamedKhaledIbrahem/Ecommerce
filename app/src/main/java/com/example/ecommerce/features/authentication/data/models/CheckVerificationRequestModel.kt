package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class CheckVerificationRequestModel(
    @SerializedName("email")
    val email: String,
    // @SerializedName("userName")
    // val userName: String,
    @SerializedName("code")
    val code: String,
)
