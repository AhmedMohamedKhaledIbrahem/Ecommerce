package com.example.ecommerce.features.userprofile.data.models

import com.google.gson.annotations.SerializedName

data class UpdateUserNameDetailsRequestModel(
    @SerializedName("id") val id :Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("name") val displayName: String,
)
