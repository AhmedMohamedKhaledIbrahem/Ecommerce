package com.example.ecommerce.features.authentication.data.datasources.remotedatasource

import com.example.ecommerce.features.authentication.data.models.AuthenticationRequestModel
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.CheckVerificationRequestModel
import com.example.ecommerce.features.authentication.data.models.EmailRequestModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.data.models.SignUpRequestModel

interface AuthenticationRemoteDataSource {
    suspend fun login(loginParams: AuthenticationRequestModel): AuthenticationResponseModel
    suspend fun signUp(signUpParams: SignUpRequestModel): MessageResponseModel
    suspend fun resetPassword(resetPasswordParams: EmailRequestModel): MessageResponseModel
    suspend fun sendVerificationCode(sendVerificationCodeParams: EmailRequestModel): MessageResponseModel
    suspend fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestModel): MessageResponseModel
}