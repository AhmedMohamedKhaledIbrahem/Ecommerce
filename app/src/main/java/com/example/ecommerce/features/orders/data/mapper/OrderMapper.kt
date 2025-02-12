package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity

object OrderMapper {
    fun mapEntityToModel(entity: OrderRequestEntity): OrderRequestModel {
        val lineItems = entity.lineItems.map { LineItemMapper.mapEntityToModel(it) }
        return OrderRequestModel(
            paymentMethod = entity.paymentMethod,
            status = entity.status,
            paymentMethodTitle = entity.paymentMethodTitle,
            setPaid = entity.setPaid,
            billing = entity.billing,
            shipping = entity.shipping,
            lineItems = lineItems,
            customerId = entity.customerId

        )
    }

    fun mapModelToEntity(model: OrderResponseModel): OrderResponseEntity {
        return OrderResponseEntity(
            id = model.id,
            status = model.status
        )
    }


}