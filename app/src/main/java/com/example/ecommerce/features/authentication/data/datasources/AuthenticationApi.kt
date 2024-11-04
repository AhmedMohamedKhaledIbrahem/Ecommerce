package com.example.ecommerce.features.authentication.data.datasources

import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("wp-json/jwt-auth/v1/token")
    suspend fun loginRequest(@Body request: AuthenticationRequestEntity): Response<AuthenticationResponseModel>


    @POST("wp-json/custom/v1/register")
    suspend fun signUpRequest(@Body request: SignUpRequestEntity): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/reset-password")
    suspend fun resetPasswordRequest(@Body request: EmailRequestEntity): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/verify-email")
    suspend fun sendVerificationCodeRequest(@Body request: EmailRequestEntity): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/check-verification-status")
    suspend fun checkVerificationCodeRequest(@Body request: CheckVerificationRequestEntity): Response<MessageResponseModel>

}