package com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled

import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import javax.inject.Inject

class IsDarkModeEnableUseCase @Inject constructor(
    val repository: PreferencesRepository
):IIsDarkModeEnableUseCase {
    override suspend fun invoke(): Boolean {
        return repository.isDarkModeEnabled()
    }

}