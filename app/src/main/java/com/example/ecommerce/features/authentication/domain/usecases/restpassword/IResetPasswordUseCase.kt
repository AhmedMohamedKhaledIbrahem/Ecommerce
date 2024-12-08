package com.example.ecommerce.features.authentication.domain.usecases.reestpassword

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity

interface IResetPasswordUseCase {
    suspend operator fun invoke(resetPasswordParams: EmailRequestEntity): MessageResponseEntity
}