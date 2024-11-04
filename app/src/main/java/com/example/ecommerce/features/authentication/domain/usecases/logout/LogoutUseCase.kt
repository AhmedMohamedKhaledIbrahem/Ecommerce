package com.example.ecommerce.features.authentication.domain.usecases.logout

import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) : ILogoutUseCase {
    override suspend operator fun invoke() {
        return repository.logout()
    }
}