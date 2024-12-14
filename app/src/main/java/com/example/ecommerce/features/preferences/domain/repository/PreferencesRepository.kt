package com.example.ecommerce.features.preferences.domain.repository

interface PreferencesRepository {
    suspend fun isDarkModeEnabled(): Boolean
    suspend fun setDarkModeEnabled(isDarkModeEnabled: Boolean)
    suspend fun getLanguage(): String
    suspend fun setLanguage(languageCode: String)
}