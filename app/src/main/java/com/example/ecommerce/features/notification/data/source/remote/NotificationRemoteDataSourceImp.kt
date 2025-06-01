package com.example.ecommerce.features.notification.data.source.remote

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.notification.data.model.NotificationRequestModel
import com.example.ecommerce.features.notification.data.model.NotificationResponseModel
import org.json.JSONObject
import javax.inject.Inject

class NotificationRemoteDataSourceImp @Inject constructor(
    private val api: FcmApi
) : NotificationRemoteDataSource {


    override suspend fun saveToken(notificationRequestParams: NotificationRequestModel): NotificationResponseModel {
        return try {
            val response = api.saveToken(notificationRequestParams)
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