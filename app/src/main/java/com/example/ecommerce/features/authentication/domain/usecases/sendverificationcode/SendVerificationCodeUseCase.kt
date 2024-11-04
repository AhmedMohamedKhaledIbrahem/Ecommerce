package com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode

import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class SendVerificationCodeUseCase @Inject constructor(private val repository: AuthenticationRepository) :
    ISendVerificationCodeUseCase {
    override suspend fun invoke(sendVerificationCodeParams: EmailRequestEntity): MessageResponseEntity {
        return repository.sendVerificationCode(sendVerificationCodeParams = sendVerificationCodeParams)
    }
}