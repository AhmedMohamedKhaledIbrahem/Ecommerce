package com.example.ecommerce.features.preferences.presentation.event

sealed class PreferencesEvent {
    sealed class Input : PreferencesEvent() {
        data class SetLanguage(val languageCode: String) : Input()
        data class SetDarkMode(val isDarkMode: Boolean) : Input()
    }

    sealed class Get : PreferencesEvent() {
        data object GetLanguage : Get()
        data object GetDarkMode : Get()
    }

    sealed class Button : PreferencesEvent() {
        data object LanguageButton : PreferencesEvent()
        data object DarkModeButton : PreferencesEvent()
    }
}