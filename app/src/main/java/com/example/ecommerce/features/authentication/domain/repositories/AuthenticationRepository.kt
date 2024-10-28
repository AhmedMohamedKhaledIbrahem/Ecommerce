package com.example.ecommerce.features.authentication.domain.repositories

import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity

interface AuthenticationRepository {
    suspend fun login(loginParams: AuthenticationRequestEntity): AuthenticationResponseEntity
    suspend fun signUp(singUpParams: SignUpRequestEntity): MessageResponseEntity
    suspend fun resetPassword(email:String): MessageResponseEntity
    suspend fun logout()
}