package com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode

import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity

interface ISendVerificationCodeUseCase {
    suspend operator fun invoke(sendVerificationCodeParams: EmailRequestEntity): MessageResponseEntity
}