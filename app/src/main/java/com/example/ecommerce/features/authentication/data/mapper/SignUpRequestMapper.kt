package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.SignUpRequestModel
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity

object SignUpRequestMapper {
    fun mapToModel(entity: SignUpRequestEntity):SignUpRequestModel{
        return SignUpRequestModel(
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            username = entity.username,
            password = entity.password
        )
    }
}