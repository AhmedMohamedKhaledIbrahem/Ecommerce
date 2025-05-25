package com.example.ecommerce.features.preferences.presentation.state

import java.util.Locale

data class PreferencesState(
    val languageCode: String =Locale.getDefault().language,
    val isDarkMode: Boolean = false,
    val isFinished: Boolean = false,
)
