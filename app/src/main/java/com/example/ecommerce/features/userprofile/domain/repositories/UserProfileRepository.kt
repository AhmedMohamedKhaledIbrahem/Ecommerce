package com.example.ecommerce.features.userprofile.domain.repositories

import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import java.io.File

interface UserProfileRepository {
    suspend fun getImageProfileById(userId: Int): GetImageProfileResponseEntity
    suspend fun getUserProfile(): UserEntity
    suspend fun getUserNameDetails()
    suspend fun uploadImageProfile(image: File): UploadImageProfileResponseEntity
    suspend fun updateUserNameDetails(updateUserNameDetailsParams: UpdateUserNameDetailsRequestEntity)

}