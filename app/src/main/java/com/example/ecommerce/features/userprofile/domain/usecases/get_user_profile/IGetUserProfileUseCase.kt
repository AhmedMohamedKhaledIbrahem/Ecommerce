package com.example.ecommerce.features.userprofile.domain.usecases.get_user_profile

import com.example.ecommerce.core.database.data.entities.user.UserEntity

interface IGetUserProfileUseCase {
    suspend operator fun invoke(): UserEntity
}