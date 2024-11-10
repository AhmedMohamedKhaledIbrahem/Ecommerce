package com.example.ecommerce.features.userprofile.data.datasources.remotedatasource

import com.example.ecommerce.features.userprofile.data.models.CheckUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import java.io.File

interface UserProfileRemoteDataSource {
    suspend fun getImageProfileById(userId: Int): GetImageProfileResponseModel
    suspend fun getUserNameDetails():UpdateUserNameDetailsResponseModel
    suspend fun uploadImageProfile(image: File): UploadImageProfileResponseModel
    suspend fun updateUserNameDetails(
        updateUserNameDetailsRequestEntity: UpdateUserNameDetailsRequestEntity
    ): UpdateUserNameDetailsResponseModel
    suspend fun checkUserNameDetailsUpdate():CheckUserNameDetailsResponseModel
}