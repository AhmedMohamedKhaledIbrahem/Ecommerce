package com.example.ecommerce.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class AuthenticationResponseModel(
    @SerializedName("token")
    val token :String,
    @SerializedName("user_id")
    val userId :Int,
    @SerializedName("user_email")
    val email:String,
    @SerializedName("user_nicename")
    val userName:String,
    @SerializedName("user_display_name")
    val userDisplayName:String,
    @SerializedName("first_name")
    val firstName :String,
    @SerializedName("last_name")
    val lastName :String,
    @SerializedName("roles")
    val roles : List<String>,
    @SerializedName("exp")
    val expiredToken :Int
)
