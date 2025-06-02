package com.example.ecommerce.core.database.data.mapper

import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel

object UserMapper {
    fun mapToEntity(model: AuthenticationResponseModel): UserEntity {
        return UserEntity(
            id = 0,
            userId = model.userId,
            userName = model.userName,
            userEmail = model.email,
            firstName = model.firstName,
            lastName = model.lastName,
            roles = model.roles.joinToString(","),
            verificationStatues = model.verificationStatues,
            displayName = model.userDisplayName,
            phone = model.phone,
            expiredToken = model.expiredToken,
        )
    }
}