package com.example.ecommerce.features.userprofile.data.models

import com.google.gson.annotations.SerializedName

data class UpdateUserNameDetailsResponseModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val displayName: String? = null ?: "",
    @SerializedName("first_name") val firstName: String? = null ?: "",
    @SerializedName("last_name") val lastName: String? = null ?: ""
)
