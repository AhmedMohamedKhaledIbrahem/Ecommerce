package com.example.ecommerce.features.preferences.data.datasource.localdatasource

import android.content.SharedPreferences
import com.example.ecommerce.core.errors.FailureException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class PreferencesLocalDataSourceImp @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PreferencesLocalDataSource {
    override suspend fun isDarkModeEnabled(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getBoolean("dark_mode", false)
            } catch (e: Exception) {
                throw FailureException(e.message ?: "unknown error")
            }
        }
    }

    override suspend fun setDarkModeEnabled(isDarkModeEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit().putBoolean("dark_mode", isDarkModeEnabled).apply()
            } catch (e: Exception) {
                throw FailureException(e.message ?: "unknown error")
            }
        }
    }

    override suspend fun getLanguage(): String {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferences.getString("language", Locale.getDefault().language)
                    ?: Locale.getDefault().language
            } catch (e: Exception) {
                throw FailureException(e.message ?: "unknown error")
            }
        }
    }

    override suspend fun setLanguage(languageCode: String) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferences.edit().putString("language",languageCode).apply()
            } catch (e: Exception) {
                throw FailureException(e.message ?: "unknown error")
            }
        }
    }
}