package com.example.ecommerce.features.product.data.model

import com.google.gson.annotations.SerializedName

data class StockResponseModel(
    @SerializedName("is_in_stock") val isStock: Boolean,
    @SerializedName("stock_status") val statusStock: String,
)
