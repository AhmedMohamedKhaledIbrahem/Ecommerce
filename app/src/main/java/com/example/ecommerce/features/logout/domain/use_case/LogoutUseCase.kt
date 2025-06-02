package com.example.ecommerce.features.logout.domain.use_case

import com.example.ecommerce.features.logout.domain.repository.LogoutRepository
import javax.inject.Inject

interface LogoutUseCase {
    suspend operator fun invoke(fcmTokenParams: String)
}

class LogoutUseCaseImp @Inject constructor(private val repository: LogoutRepository) :
    LogoutUseCase {
    override suspend fun invoke(fcmTokenParams: String) {
        repository.logout(fcmTokenParams = fcmTokenParams)
    }

}