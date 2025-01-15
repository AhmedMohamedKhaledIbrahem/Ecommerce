package com.example.ecommerce.features.cart.data.mapper

import com.example.ecommerce.features.cart.data.models.AddItemRequestModel
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity

object AddItemRequestMapper {
    fun mapToModel(addItemRequestEntity: AddItemRequestEntity): AddItemRequestModel {
        return AddItemRequestModel(
            id = addItemRequestEntity.id,
            quantity = addItemRequestEntity.quantity
        )
    }
}