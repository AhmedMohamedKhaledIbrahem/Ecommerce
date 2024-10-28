package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity

object MessageResponseMapper {
    fun mapToEntity(model: MessageResponseModel): MessageResponseEntity {
        return MessageResponseEntity(
            message = model.message
        )
    }
}