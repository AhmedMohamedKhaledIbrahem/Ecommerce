package com.example.ecommerce.features.userprofile.domain.repositories

import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import java.io.File

interface UserProfileRepository {
    suspend fun getImageProfileById(userId: Int): GetImageProfileResponseEntity
    suspend fun getUserProfile(): UserEntity
    suspend fun fetchUpdateUserDetails(): String
    suspend fun uploadImageProfile(image: File): UploadImageProfileResponseEntity
    suspend fun updateUserDetails(updateUserDetailsParams: UpdateUserDetailsRequestEntity)

}