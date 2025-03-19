package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class AddressDataResponseModel(
    @SerializedName("billing") val billing: BillingInfoResponseModel? = null
        ?: BillingInfoResponseModel(),

)
