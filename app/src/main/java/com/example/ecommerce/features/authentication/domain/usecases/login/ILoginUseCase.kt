package com.example.ecommerce.features.authentication.domain.usecases.login

import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity

interface ILoginUseCase {
    suspend operator fun invoke(loginParams: AuthenticationRequestEntity): AuthenticationResponseEntity
}