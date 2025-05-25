package com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile

import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import java.io.File
import javax.inject.Inject

class UploadImageProfileUseCase @Inject constructor(private val repository: UserProfileRepository)
    :IUploadImageProfileUseCase{
    override suspend fun invoke(image: File): UploadImageProfileResponseEntity {
        return repository.uploadImageProfile(image = image)
    }
}