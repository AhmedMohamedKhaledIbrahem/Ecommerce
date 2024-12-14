package com.example.ecommerce.features.preferences.presentation.viewmodel

import com.example.ecommerce.core.state.UiState
import kotlinx.coroutines.flow.SharedFlow

interface IPreferencesViewModel {
    val preferencesState: SharedFlow<UiState<Any>>
    fun getLanguage()
    fun setLanguage(languageCode: String)
    fun isDarkModeEnabled()
    fun setDarkModeEnabled(isDarkModeEnabled: Boolean)
    fun <T> preferencesUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}