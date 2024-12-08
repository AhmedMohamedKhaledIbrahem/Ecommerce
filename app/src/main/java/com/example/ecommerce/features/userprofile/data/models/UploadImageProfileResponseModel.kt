package com.example.ecommerce.features.userprofile.data.models

import com.google.gson.annotations.SerializedName

data class UploadImageProfileResponseModel(
    @SerializedName("message") val message:String ,
    @SerializedName("file") val imageLink:String,
    @SerializedName("user_id") val userid:Int
)
