package com.example.ecommerce.features.orders.domain.entities

import com.google.gson.annotations.SerializedName

data class OrderResponseEntity(
    val id: Int,
    val status: String,
    val currency: String,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("payment_method_title") val paymentMethodTitle: String,
    @SerializedName("date_created") val dateCreate: String,
    @SerializedName("total") val totalPrice: String,
    @SerializedName("number") val orderTagNumber: String,
    @SerializedName("line_items") val lineItems: List<LineItemResponseEntity>
)
