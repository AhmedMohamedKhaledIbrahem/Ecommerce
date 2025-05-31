package com.example.ecommerce.features.authentication.data.datasources

import com.example.ecommerce.features.authentication.data.models.AuthenticationRequestModel
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.ChangePasswordRequestModel
import com.example.ecommerce.features.authentication.data.models.CheckVerificationRequestModel
import com.example.ecommerce.features.authentication.data.models.ConfirmPasswordResetRequestModel
import com.example.ecommerce.features.authentication.data.models.EmailRequestModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.data.models.SignUpRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("wp-json/jwt-auth/v1/token")
    suspend fun loginRequest(@Body request: AuthenticationRequestModel): Response<AuthenticationResponseModel>
    
    @POST("wp-json/custom/v1/register")
    suspend fun signUpRequest(@Body request: SignUpRequestModel): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/forget-password")
    suspend fun resetPasswordRequest(@Body request: EmailRequestModel): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/verify-email")
    suspend fun sendVerificationCodeRequest(@Body request: EmailRequestModel): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/check-verification-status")
    suspend fun checkVerificationCodeRequest(@Body request: CheckVerificationRequestModel): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/change-password")
    suspend fun changePasswordRequest(@Body request: ChangePasswordRequestModel): Response<MessageResponseModel>

    @POST("wp-json/custom/v1/confirm-change-password")
    suspend fun confirmPasswordReset(@Body request: ConfirmPasswordResetRequestModel): Response<MessageResponseModel>

}