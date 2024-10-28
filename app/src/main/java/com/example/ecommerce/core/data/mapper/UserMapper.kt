package com.example.ecommerce.core.data.mapper

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel

object UserMapper {
    fun mapToEntity(model:AuthenticationResponseModel):UserEntity{
        return UserEntity(
            id = 0,
            userId = model.userId ,
            userName = model.userName,
            userEmail = model.email ,
            firstName = model.firstName ,
            lastName = model.lastName,
            displayName = model.userDisplayName,
            expiredToken = model.expiredToken ,

        )
    }
}