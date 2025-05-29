package com.example.ecommerce.features.userprofile.domain.usecases.get_image_profile_by_id

import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import javax.inject.Inject

class GetImageProfileByIdUseCase @Inject constructor(private val repository: UserProfileRepository) :
    IGetImageProfileByIdUseCase {
    override suspend fun invoke(userId: Int): GetImageProfileResponseEntity {
        return repository.getImageProfileById(userId = userId)
    }
}