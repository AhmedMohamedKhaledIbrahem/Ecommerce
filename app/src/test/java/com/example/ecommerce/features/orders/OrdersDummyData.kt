package com.example.ecommerce.features.orders

import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderTagEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.orders.data.mapper.OrderItemMapper
import com.example.ecommerce.features.orders.data.mapper.OrderMapper
import com.example.ecommerce.features.orders.data.mapper.OrderTagMapper
import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson

val tCreateOrderResponseModelJson: OrderResponseModel = fixture("createdOrder.json").run {
    Gson().fromJson(this, OrderResponseModel::class.java)
}

val tOrderTagEntityByMapper = OrderTagMapper.mapToEntity(tCreateOrderResponseModelJson)
val tOrderResponseEntity = OrderMapper.mapModelToEntity(tCreateOrderResponseModelJson)
val tOrderItemEntityByMapper = tCreateOrderResponseModelJson.lineItems.map {
    OrderItemMapper.mapToEntity(it, tCreateOrderResponseModelJson.id)
}
val tOrderTagEntity = OrderTagEntity(
    orderId = 1,
    status = "processing",
    currency = "USD",
    paymentMethod = "cod",
    paymentMethodTitle = "Cash on Delivery",
    orderTagNumber = "1",
    totalPrice = "57.50",
    dateCreatedOrder = "2024-02-13T10:45:32",
)
val tOrderItemEntity = listOf(
    OrderItemEntity(
        lineItemId = 101,
        orderId = 1,
        itemName = "Sample Product1",
        productId = 123,
        priceItem = "50.00",
        quantity = 1,
        image = "test1.com"
    ),
    OrderItemEntity(
        lineItemId = 102,
        orderId = 1,
        itemName = "Sample Product2",
        productId = 456,
        priceItem = "7.50",
        quantity = 2,
        image = "test2.com"
    )
)
val tOrderWithItems = OrderWithItems(
    order = tOrderTagEntity,
    items = tOrderItemEntity
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





