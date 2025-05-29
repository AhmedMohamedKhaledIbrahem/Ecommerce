package com.example.ecommerce.features.userprofile.presentation.state

import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import java.io.File

data class ImageProfileState(
    val isUploadImageLoading: Boolean = false,
    val file: File = File(""),
    val userId: Int = 0,
    val uploadImageProfileResponseEntity: UploadImageProfileResponseEntity? = null,
    val getImageProfileResponseEntity: GetImageProfileResponseEntity? = null
)
