package com.example.ecommerce.features.authentication.data.datasources.remotedatasource

import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity

interface AuthenticationRemoteDataSource {
    suspend fun login(loginParams: AuthenticationRequestEntity): AuthenticationResponseModel
    suspend fun signUp(signUpParams: SignUpRequestEntity): MessageResponseModel
    suspend fun resetPassword(resetPasswordParams: EmailRequestEntity): MessageResponseModel
    suspend fun sendVerificationCode(sendVerificationCodeParams: EmailRequestEntity): MessageResponseModel
    suspend fun checkVerificationCode(checkVerificationCodeParams: CheckVerificationRequestEntity): MessageResponseModel
}