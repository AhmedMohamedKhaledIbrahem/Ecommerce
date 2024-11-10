package com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid

import com.example.ecommerce.core.data.entities.UserEntity

interface IGetUserProfileByIdUseCase {
    suspend operator fun invoke():UserEntity
}