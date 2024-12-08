package com.example.ecommerce.features.userprofile.data.mapper

import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity

object GetImageProfileMapper {
    fun mapToEntity(model:GetImageProfileResponseModel):GetImageProfileResponseEntity{
        return GetImageProfileResponseEntity(
            userId = model.userId,
            profileImage = model.profileImage,
        )
    }
}