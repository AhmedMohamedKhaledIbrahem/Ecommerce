package com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid

import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity

interface IGetImageProfileByIdUseCase {
    suspend operator fun invoke(userId:Int):GetImageProfileResponseEntity
}