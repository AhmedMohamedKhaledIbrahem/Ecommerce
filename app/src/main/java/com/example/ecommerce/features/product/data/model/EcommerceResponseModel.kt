package com.example.ecommerce.features.product.data.model

import com.google.gson.annotations.SerializedName

data class EcommerceResponseModel(
   @SerializedName("products") val products: List<ProductResponseModel>,
)
