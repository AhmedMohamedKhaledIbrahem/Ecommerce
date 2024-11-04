package com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode

import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class CheckVerificationCodeUseCase @Inject constructor(private val repository: AuthenticationRepository) :
    ICheckVerificationCodeUseCase {
    override suspend fun invoke(checkVerificationCodeParams: CheckVerificationRequestEntity): MessageResponseEntity {
        return repository.checkVerificationCode(checkVerificationCodeParams = checkVerificationCodeParams)
    }

}