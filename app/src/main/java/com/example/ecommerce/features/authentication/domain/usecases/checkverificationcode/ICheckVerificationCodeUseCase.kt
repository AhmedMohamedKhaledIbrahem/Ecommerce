package com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode

import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity

interface ICheckVerificationCodeUseCase {
    suspend operator fun invoke(checkVerificationCodeParams: CheckVerificationRequestEntity): MessageResponseEntity
}