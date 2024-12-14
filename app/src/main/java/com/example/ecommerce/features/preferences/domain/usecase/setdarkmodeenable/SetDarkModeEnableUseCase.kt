package com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable

import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetDarkModeEnableUseCase @Inject constructor(
    val repository: PreferencesRepository
):ISetDarkModeEnableUseCase {
    override suspend fun invoke(isDarkModeEnabled: Boolean) {
        repository.setDarkModeEnabled(isDarkModeEnabled=isDarkModeEnabled)
    }

}