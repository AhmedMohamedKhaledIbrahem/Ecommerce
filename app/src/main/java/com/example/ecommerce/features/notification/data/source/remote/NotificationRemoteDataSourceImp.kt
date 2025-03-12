package com.example.ecommerce.features.notification.data.source.remote

import com.example.ecommerce.core.errors.FailureException
import org.json.JSONObject
import javax.inject.Inject

class NotificationRemoteDataSourceImp @Inject constructor(
    private val api: FcmApi
) : NotificationRemoteDataSource {

    override suspend fun saveToken(token: String) {
        try {
            val response  = api.saveToken(mapOf("token" to token))
            if (!response.isSuccessful){
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody?.let {
                    JSONObject(it).optString("status","Unknown Error")
                } ?: "Unknown Error"
                throw FailureException(errorMessage)
            }
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }


}