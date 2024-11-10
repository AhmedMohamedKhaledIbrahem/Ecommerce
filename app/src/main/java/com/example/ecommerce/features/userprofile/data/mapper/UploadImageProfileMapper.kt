package com.example.ecommerce.features.userprofile.data.mapper

import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity

object UploadImageProfileMapper {
    fun mapToEntity(model: UploadImageProfileResponseModel): UploadImageProfileResponseEntity {
        return UploadImageProfileResponseEntity(
            message = model.message
        )
    }
}