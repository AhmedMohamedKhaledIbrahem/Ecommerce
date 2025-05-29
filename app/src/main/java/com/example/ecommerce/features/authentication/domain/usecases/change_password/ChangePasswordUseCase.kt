package com.example.ecommerce.features.authentication.domain.usecases.change_password

import com.example.ecommerce.features.authentication.domain.entites.ChangePasswordRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

interface ChangePasswordUseCase {
    suspend operator fun invoke(changePasswordParams: ChangePasswordRequestEntity): MessageResponseEntity
}

class ChangePasswordUseCaseImp @Inject constructor(
    private val repository: AuthenticationRepository
) : ChangePasswordUseCase {
    override suspend fun invoke(changePasswordParams: ChangePasswordRequestEntity): MessageResponseEntity {
        return repository.changePassword(changePasswordParams)
    }

}