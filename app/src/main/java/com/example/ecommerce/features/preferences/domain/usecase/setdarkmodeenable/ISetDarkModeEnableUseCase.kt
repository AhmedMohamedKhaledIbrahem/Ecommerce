package com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable

interface ISetDarkModeEnableUseCase {
    suspend operator fun invoke(isDarkModeEnabled: Boolean)
}