package com.example.ecommerce.features.userprofile.data.datasources.localdatasource

import com.example.ecommerce.core.data.entities.UserEntity
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity

interface UserProfileLocalDataSource {
    suspend fun updateUserNameDetails(updateUserNameDetailsRequestEntity: UpdateUserNameDetailsRequestEntity)
    suspend fun getUserProfile(): UserEntity
}