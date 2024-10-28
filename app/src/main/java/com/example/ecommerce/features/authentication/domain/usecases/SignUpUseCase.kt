package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthenticationRepository){
    suspend operator fun invoke(signUpParams:SignUpRequestEntity):MessageResponseEntity{
        return repository.signUp(singUpParams = signUpParams)
    }
}