package com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid

import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class GetImageProfileByIdUseCase @Inject constructor(private val repository: UserProfileRepository) :
    IGetImageProfileByIdUseCase {
    override suspend fun invoke(userId: Int): GetImageProfileResponseEntity {
        return repository.getImageProfileById(userId = userId)
    }
}