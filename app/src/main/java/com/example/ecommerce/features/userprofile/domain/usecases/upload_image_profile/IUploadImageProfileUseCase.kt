package com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile

import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import java.io.File

interface IUploadImageProfileUseCase {
    suspend operator fun invoke(image: File):UploadImageProfileResponseEntity
}