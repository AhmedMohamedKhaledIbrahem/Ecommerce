package com.example.ecommerce.features.authentication.data.mapper

import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity

object AuthenticationMapper {
    fun mapToEntity(model:AuthenticationResponseModel):AuthenticationResponseEntity{
        return AuthenticationResponseEntity(
            token = model.token,
            userName = model.userName,
            displayName = model.userDisplayName,
            userEmail = model.email,
            roles = model.roles,
            firstName = model.firstName,
            lastName = model.lastName,
            userId = model.userId,
            verificationStatues = model.verificationStatues,
            expiredToken = model.expiredToken
        )
    }
}