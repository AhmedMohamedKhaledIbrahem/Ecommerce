package com.example.ecommerce.features.authentication.domain.entites

import com.google.gson.annotations.SerializedName

data class AuthenticationRequestEntity(
  @SerializedName("username")  val userName:String ,
  @SerializedName("password")  val password:String
)
