package com.example.ecommerce.features.product.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyCodeResponseModel(
    @SerializedName("currency_code") val currencyCode: String? = null ?: "eg",
)
