package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.core.database.data.entities.orders.OrderTagEntity
import com.example.ecommerce.features.orders.data.models.OrderResponseModel

object OrderTagMapper {
    fun mapToEntity(orderTagResponseModel: OrderResponseModel): OrderTagEntity {
        return OrderTagEntity(
            orderId = orderTagResponseModel.id,
            status = orderTagResponseModel.status,
            paymentMethod = orderTagResponseModel.paymentMethod,
            paymentMethodTitle = orderTagResponseModel.paymentMethodTitle,
            dateCreatedOrder = orderTagResponseModel.dateCreate,
            orderTagNumber = orderTagResponseModel.id.toString(),
            totalPrice = orderTagResponseModel.totalPrice,
            currency = orderTagResponseModel.currency
        )

    }
}