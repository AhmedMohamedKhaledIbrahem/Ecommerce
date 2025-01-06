package com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid

import com.example.ecommerce.core.database.data.entities.user.UserEntity

interface IGetUserProfileUseCase {
    suspend operator fun invoke(): UserEntity
}