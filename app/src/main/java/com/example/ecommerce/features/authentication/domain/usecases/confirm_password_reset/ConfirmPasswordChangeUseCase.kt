package com.example.ecommerce.features.authentication.domain.usecases.confirm_password_reset

import com.example.ecommerce.features.authentication.domain.entites.ConfirmPasswordResetRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

interface ConfirmPasswordChangeUseCase {
    suspend operator fun invoke(confirmPasswordParams: ConfirmPasswordResetRequestEntity): MessageResponseEntity
}

class ConfirmPasswordChangeUseCaseImp @Inject constructor(
    private val repository: AuthenticationRepository
) : ConfirmPasswordChangeUseCase {
    override suspend fun invoke(confirmPasswordParams: ConfirmPasswordResetRequestEntity): MessageResponseEntity {
        return repository.confirmPasswordChange(confirmPasswordParams)
    }

}