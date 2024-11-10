package com.example.ecommerce.features.userprofile.data.models

import com.google.gson.annotations.SerializedName

data class GetImageProfileResponseModel(
    @SerializedName("user_id")
    val userId:Int,
    @SerializedName("profile_image")
    val profileImage:String
)
