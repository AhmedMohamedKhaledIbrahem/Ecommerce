package com.example.ecommerce.features.address.data.models

import com.google.gson.annotations.SerializedName

data class BillingInfoRequestModel(
    @SerializedName("first_name") val firstName: String =  "",
    @SerializedName("last_name") val lastName: String =  "",
    @SerializedName("address_1") val address: String =  "",
    @SerializedName("city") val city: String =  "",
    @SerializedName("postcode") val postCode: String =  "",
    @SerializedName("country") val country: String =  "",
    @SerializedName("email") val email: String =  "",
    @SerializedName("phone") val phone: String =  ""
)
