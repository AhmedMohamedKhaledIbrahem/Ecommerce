package com.example.ecommerce.features.authentication.domain.entites

import com.google.gson.annotations.SerializedName

data class EmailRequestEntity(
  @SerializedName("email") val email :String
)
