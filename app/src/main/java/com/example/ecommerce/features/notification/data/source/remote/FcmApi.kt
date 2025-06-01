package com.example.ecommerce.features.notification.data.source.remote


import com.example.ecommerce.features.notification.data.model.NotificationRequestModel
import com.example.ecommerce.features.notification.data.model.NotificationResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("wp-json/fcm/v1/save-token")
    suspend fun saveToken(@Body notificationRequestParams: NotificationRequestModel): Response<NotificationResponseModel>
}