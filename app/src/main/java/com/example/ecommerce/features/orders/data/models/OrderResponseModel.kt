package com.example.ecommerce.features.orders.data.models

import com.google.gson.annotations.SerializedName

data class OrderResponseModel(
    val id: Int,
    val status: String,
    val currency: String,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("payment_method_title") val paymentMethodTitle: String,
    @SerializedName("date_created") val dateCreate: String,
    @SerializedName("total") val totalPrice: String,
    @SerializedName("number") val orderTagNumber: String,
    @SerializedName("line_items") val lineItems: List<LineItemResponseModel>
)