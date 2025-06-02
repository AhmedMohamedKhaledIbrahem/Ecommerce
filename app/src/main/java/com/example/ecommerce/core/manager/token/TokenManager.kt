package com.example.ecommerce.core.manager.token

interface TokenManager {
    suspend fun saveToken(token: String)
    fun getToken(): String?
    suspend fun clearToken()
     fun saveVerificationStatus(status: Boolean)
    fun getVerificationStatus(): Boolean
    suspend fun clearVerificationStatus()
}