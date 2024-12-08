package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class AddressResponseModel(
    @SerializedName("id") val userId:Int,
    @SerializedName("message") val message: String? = null ?: "",
    @SerializedName("billing") val billing: BillingInfoResponseModel? = null
        ?: BillingInfoResponseModel(),
    @SerializedName("shipping") val shipping: ShippingInfoResponseModel? = null
        ?: ShippingInfoResponseModel()

)
