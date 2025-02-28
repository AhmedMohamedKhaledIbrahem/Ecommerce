package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.features.orders.data.models.LineItemResponseModel

object OrderItemMapper {
    fun mapToEntity(lineItemResponseModel: LineItemResponseModel, orderId: Int): OrderItemEntity {
        return OrderItemEntity(
            lineItemId = lineItemResponseModel.lineItemId,
            productId = lineItemResponseModel.productId,
            itemName = lineItemResponseModel.itemName,
            priceItem = lineItemResponseModel.totalPrice,
            image = lineItemResponseModel.image.imagePath,
            quantity = lineItemResponseModel.quantity,
            orderId = orderId
        )
    }
}