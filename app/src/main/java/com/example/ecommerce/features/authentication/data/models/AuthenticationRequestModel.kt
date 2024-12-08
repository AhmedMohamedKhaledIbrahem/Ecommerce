package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class AuthenticationRequestModel(
    @SerializedName("username")  val userName:String,
    @SerializedName("password")  val password:String
) {
}