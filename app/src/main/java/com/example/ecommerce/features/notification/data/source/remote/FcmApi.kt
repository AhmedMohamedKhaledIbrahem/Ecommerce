package com.example.ecommerce.features.notification.data.source.remote


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("wp-json/fcm/v1/save-token")
    suspend fun saveToken(@Body token: Map<String, String>): Response<ResponseBody>
}