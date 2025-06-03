package com.example.ecommerce.features.logout.data.source

import com.example.ecommerce.core.constants.REMOVE_FCM_TOKEN_END_POINT
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Query

interface LogoutApi {
    @DELETE(REMOVE_FCM_TOKEN_END_POINT)
    suspend fun removeFcmToken(@Query("token") token: String): Response<Unit>
}