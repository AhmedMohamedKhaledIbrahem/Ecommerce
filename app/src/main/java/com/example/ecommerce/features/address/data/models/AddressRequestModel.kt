package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class AddressRequestModel(
    @SerializedName("billing") val billing: BillingInfoRequestModel? = BillingInfoRequestModel(),
    @SerializedName("shipping") val shipping: ShippingInfoRequestModel? = ShippingInfoRequestModel()
)

