package com.example.ecommerce.features.logout.data.source

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Query

interface LogoutApi {
    @DELETE("wp-json/custom/v1/remove-fcm-token")
    suspend fun removeFcmToken(@Query("token") token: String): Response<Unit>
}