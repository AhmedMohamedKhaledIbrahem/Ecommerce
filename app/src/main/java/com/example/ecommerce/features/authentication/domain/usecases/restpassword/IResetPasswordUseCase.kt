package com.example.ecommerce.features.authentication.domain.usecases.restpassword

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity

interface IResetPasswordUseCase {
    suspend operator fun invoke(resetPasswordParams: EmailRequestEntity): MessageResponseEntity
}