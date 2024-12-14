package com.example.ecommerce.features.preferences.domain.usecase.setlanguage

import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: PreferencesRepository
) : ISetLanguageUseCase {
    override suspend fun invoke(languageCode: String) {
        repository.setLanguage(languageCode = languageCode)
    }
}