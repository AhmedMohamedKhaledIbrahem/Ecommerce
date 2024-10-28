package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository:AuthenticationRepository) {
    suspend operator fun invoke(loginParams: AuthenticationRequestEntity):AuthenticationResponseEntity{
        return repository.login(loginParams = loginParams)
    }
}