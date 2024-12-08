package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class CheckUpdateAddressResponseModel(

    @SerializedName("userId") val id:Int,
    @SerializedName("updated") val isUpdate:Boolean,
)