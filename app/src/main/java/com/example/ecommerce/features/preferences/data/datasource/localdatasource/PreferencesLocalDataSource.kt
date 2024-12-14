package com.example.ecommerce.features.preferences.data.datasource.localdatasource

interface PreferencesLocalDataSource {
    suspend fun isDarkModeEnabled(): Boolean
    suspend fun setDarkModeEnabled(isDarkModeEnabled: Boolean)
    suspend fun getLanguage(): String
    suspend fun setLanguage(languageCode: String)

}