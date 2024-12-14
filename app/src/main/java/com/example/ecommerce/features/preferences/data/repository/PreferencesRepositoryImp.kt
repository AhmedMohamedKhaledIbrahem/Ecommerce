package com.example.ecommerce.features.preferences.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.features.preferences.data.datasource.localdatasource.PreferencesLocalDataSource
import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(
    val localDataSource: PreferencesLocalDataSource
) : PreferencesRepository {
    override suspend fun isDarkModeEnabled(): Boolean {
        return try {
            localDataSource.isDarkModeEnabled()
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: "Unknown Cache error")
        }
    }

    override suspend fun setDarkModeEnabled(isDarkModeEnabled: Boolean) {
        try {
            localDataSource.setDarkModeEnabled(isDarkModeEnabled = isDarkModeEnabled)
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: "Unknown Cache error")
        }
    }

    override suspend fun getLanguage(): String {
        return try {
            localDataSource.getLanguage()
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: "Unknown Cache error")
        }
    }

    override suspend fun setLanguage(languageCode: String) {
        try {
            localDataSource.setLanguage(languageCode = languageCode)
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: "Unknown Cache error")
        }
    }
}