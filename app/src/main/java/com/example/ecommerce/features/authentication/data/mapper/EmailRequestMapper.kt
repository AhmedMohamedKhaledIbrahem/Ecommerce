package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.EmailRequestModel
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity

object EmailRequestMapper {
    fun mapToModel(entity:EmailRequestEntity):EmailRequestModel{
        return EmailRequestModel(
            email = entity.email
        )
    }
}