package com.example.ecommerce.features.userprofile.data.datasources.remotedatasource

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.userprofile.data.datasources.UserProfileApi
import com.example.ecommerce.features.userprofile.data.models.CheckUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.data.models.UpdateUserNameDetailsResponseModel
import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject


class UserProfileRemoteDataSourceImp @Inject constructor(
    private val api: UserProfileApi,
) : UserProfileRemoteDataSource {
    override suspend fun getImageProfileById(userId: Int): GetImageProfileResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getImageProfile(userId = userId)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun getUserNameDetails(): UpdateUserNameDetailsResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserNameDetails()
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }

        }
    }

    override suspend fun uploadImageProfile(image: File): UploadImageProfileResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val requestFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", image.name, requestFile)
                val response = api.uploadImageProfile(image = body)
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun updateUserNameDetails(
        updateUserNameDetailsRequestEntity: UpdateUserNameDetailsRequestEntity
    ): UpdateUserNameDetailsResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateUserNameDetails(
                    updateUserNameDetailsRequestEntity = updateUserNameDetailsRequestEntity
                )
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }

        }
    }

    override suspend fun checkUserNameDetailsUpdate(): CheckUserNameDetailsResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.checkUserNameDetailsUpdate()
                if (response.isSuccessful) {
                    response.body() ?: throw FailureException("Empty Response Body")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).optString("message", "Unknown error")
                    } ?: "Unknown error"
                    throw FailureException(errorMessage)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }

        }
    }


}