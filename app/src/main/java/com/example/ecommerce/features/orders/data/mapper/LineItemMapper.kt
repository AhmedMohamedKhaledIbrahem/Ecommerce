package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.features.orders.data.models.LineItemRequestModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity

object LineItemMapper {
    fun mapEntityToModel(entity: LineItemRequestEntity): LineItemRequestModel {
        return LineItemRequestModel(
            productId = entity.productId,
            quantity = entity.quantity
        )

    }
}
