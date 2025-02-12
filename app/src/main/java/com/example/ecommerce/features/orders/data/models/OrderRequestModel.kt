package com.example.ecommerce.features.orders.data.models

import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.google.gson.annotations.SerializedName

data class OrderRequestModel(
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("payment_method_title") val paymentMethodTitle: String,
    @SerializedName("status") val status :String = "processing",
    @SerializedName("set_paid") val setPaid: Boolean,
    @SerializedName("billing") val billing: BillingInfoRequestEntity,
    @SerializedName("shipping") val shipping: ShippingInfoRequestEntity,
    @SerializedName("line_items") val lineItems: List<LineItemRequestModel>,
    @SerializedName("customer_id") val customerId: Int
)
