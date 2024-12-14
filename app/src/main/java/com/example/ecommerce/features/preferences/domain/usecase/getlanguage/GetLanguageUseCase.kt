package com.example.ecommerce.features.preferences.domain.usecase.getlanguage

import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    val repository: PreferencesRepository
): IGetLanguageUseCase  {
    override suspend fun invoke(): String {
        return repository.getLanguage()
    }
}