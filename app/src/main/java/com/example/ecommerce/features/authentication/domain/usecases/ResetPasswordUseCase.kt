package com.example.ecommerce.features.authentication.domain.usecases

import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val repository: AuthenticationRepository) {
    suspend operator fun invoke(email:String): MessageResponseEntity {
        return repository.resetPassword(email = email)
    }
}