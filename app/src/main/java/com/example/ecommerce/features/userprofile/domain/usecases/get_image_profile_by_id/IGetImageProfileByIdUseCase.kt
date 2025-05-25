package com.example.ecommerce.features.userprofile.domain.usecases.get_image_profile_by_id

import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity

interface IGetImageProfileByIdUseCase {
    suspend operator fun invoke(userId:Int):GetImageProfileResponseEntity
}