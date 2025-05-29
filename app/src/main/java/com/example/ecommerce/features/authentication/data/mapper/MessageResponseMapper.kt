package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity

object MessageResponseMapper {
    fun mapToEntity(model: MessageResponseModel): MessageResponseEntity {
        return MessageResponseEntity(
            message = model.message,
            verified = model.verified
        )
    }
}

fun MessageResponseModel.toDomain(): MessageResponseEntity {
    return MessageResponseEntity(
        message = this.message,
        verified = this.verified
    )

}