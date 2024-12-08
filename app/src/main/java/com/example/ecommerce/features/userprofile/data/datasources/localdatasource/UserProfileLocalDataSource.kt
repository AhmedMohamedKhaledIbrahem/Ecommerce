package com.example.ecommerce.features.userprofile.data.datasources.localdatasource

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel

interface UserProfileLocalDataSource {
    suspend fun updateUserNameDetails(updateUserNameDetailsParams: UpdateUserNameDetailsResponseModel)
    suspend fun updateImageUserProfile(image:String ,userId:Int)
    suspend fun getUserProfile(): UserEntity
}