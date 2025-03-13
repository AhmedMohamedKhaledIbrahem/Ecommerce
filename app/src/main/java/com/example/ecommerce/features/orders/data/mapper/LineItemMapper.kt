package com.example.ecommerce.features.orders.data.mapper

import com.example.ecommerce.features.orders.data.models.LineItemRequestModel
import com.example.ecommerce.features.orders.data.models.LineItemResponseModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.LineItemResponseEntity

object LineItemMapper {
    fun mapEntityToModel(entity: LineItemRequestEntity): LineItemRequestModel {
        return LineItemRequestModel(
            productId = entity.productId,
            quantity = entity.quantity
        )

    }

    fun mapModelToEntity(model: LineItemResponseModel): LineItemResponseEntity {
       // val image = ImageItemMapper.mapModelToEntity(model.image)
        return LineItemResponseEntity(
            lineItemId = model.lineItemId,
            productId = model.productId,
            itemName = model.itemName,
            totalPrice = model.totalPrice,
            quantity = model.quantity,
            //image = image
        )
    }
    fun mapEntityToModel(entity: LineItemResponseEntity): LineItemResponseModel {
       // val image = ImageItemMapper.mapEntityToModel(entity.image)
        return LineItemResponseModel(
            lineItemId = entity.lineItemId,
            productId = entity.productId,
            itemName = entity.itemName,
            totalPrice = entity.totalPrice,
            quantity = entity.quantity,
          //  image = image
        )

    }
}
