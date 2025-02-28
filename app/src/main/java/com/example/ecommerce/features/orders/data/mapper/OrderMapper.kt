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
        val lineItems = model.lineItems.map { LineItemMapper.mapModelToEntity(it) }
        return OrderResponseEntity(
            id = model.id,
            orderTagNumber = model.orderTagNumber,
            totalPrice = model.totalPrice,
            paymentMethod = model.paymentMethod,
            paymentMethodTitle = model.paymentMethodTitle,
            status = model.status,
            dateCreate = model.dateCreate,
            currency = model.currency,
            lineItems = lineItems
        )
    }

    fun mapEntityToModel(entity: OrderResponseEntity): OrderResponseModel {
        val lineItems = entity.lineItems.map { LineItemMapper.mapEntityToModel(it) }
        return OrderResponseModel(
            id = entity.id,
            orderTagNumber = entity.orderTagNumber,
            totalPrice = entity.totalPrice,
            paymentMethod = entity.paymentMethod,
            paymentMethodTitle = entity.paymentMethodTitle,
            status = entity.status,
            dateCreate = entity.dateCreate,
            currency = entity.currency,
            lineItems = lineItems
        )
    }


}