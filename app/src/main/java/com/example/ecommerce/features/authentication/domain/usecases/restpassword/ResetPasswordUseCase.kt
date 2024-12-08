package com.example.ecommerce.features.authentication.domain.usecases.restpassword

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val repository: AuthenticationRepository) :
    IResetPasswordUseCase {
    override suspend operator fun invoke(resetPasswordParams: EmailRequestEntity): MessageResponseEntity {
        return repository.resetPassword(resetPasswordParams = resetPasswordParams)
    }
}