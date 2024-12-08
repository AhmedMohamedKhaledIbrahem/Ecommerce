package com.example.ecommerce.features.userprofile.domain.usecases.uploadimageprofile

import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import java.io.File

interface IUploadImageProfileUseCase {
    suspend operator fun invoke(image: File):UploadImageProfileResponseEntity
}