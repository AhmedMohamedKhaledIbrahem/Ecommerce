package com.example.ecommerce.features.logout.domain.repository

interface LogoutRepository {
    suspend fun logout(fcmTokenParams: String)
}