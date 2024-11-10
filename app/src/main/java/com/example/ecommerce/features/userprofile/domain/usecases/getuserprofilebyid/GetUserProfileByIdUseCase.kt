package com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class GetUserProfileByIdUseCase @Inject constructor( private val repository: UserProfileRepository)
    :IGetUserProfileByIdUseCase{
    override suspend fun invoke(): UserEntity {
        return repository.getUserProfile()
    }
}