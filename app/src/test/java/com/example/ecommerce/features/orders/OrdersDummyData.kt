package com.example.ecommerce.features.orders

import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.orders.data.mapper.OrderMapper
import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import okhttp3.ResponseBody.Companion.toResponseBody

val tCreateOrderResponseModelJson: OrderResponseModel = fixture("createdOrder.json").run {
    Gson().fromJson(this, OrderResponseModel::class.java)
}
val tCreateOrderResponseEntity = OrderResponseEntity(
    id = 1,
    status = "processing"
)

val tShippingInfoRequestEntity = ShippingInfoRequestEntity(
    firstName = "John",
    lastName = "Doe",
    address = "123 Main St",
    city = "Springfield",
    state = "IL",
    postCode = "62701",
    country = "US",
)
val tBillingInfoRequestEntity = BillingInfoRequestEntity(
    firstName = "John",
    lastName = "Doe",
    address = "123 Main St",
    city = "Springfield",
    state = "IL",
    postCode = "62701",
    country = "US",
    email = "billing@example.com",
    phone = "555-555-5555"
)

const val customerId: Int = 1
val tLineItemsRequestEntity = listOf(
    LineItemRequestEntity(
        productId = 123,
        quantity = 2
    ),
    LineItemRequestEntity(
        productId = 456,
        quantity = 1
    ),
)

val tCreateOrderRequestEntity = OrderRequestEntity(
    paymentMethod = "cod",
    paymentMethodTitle = "Cash on Delivery",
    status = "processing",
    lineItems = tLineItemsRequestEntity,
    setPaid = false,
    billing = tBillingInfoRequestEntity,
    shipping = tShippingInfoRequestEntity,
    customerId = customerId
)

val tCreateOrderRequestModel = OrderMapper.mapEntityToModel(tCreateOrderRequestEntity)

val tCreateOrderRequestModelJson: OrderRequestModel = fixture("createOrder.json").run {
    Gson().fromJson(this, OrderRequestModel::class.java)
}





