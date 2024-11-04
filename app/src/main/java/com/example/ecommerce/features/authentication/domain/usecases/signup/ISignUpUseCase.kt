package com.example.ecommerce.features.authentication.domain.usecases.signup

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity

interface ISignUpUseCase {
    suspend operator fun invoke(signUpParams: SignUpRequestEntity): MessageResponseEntity
}