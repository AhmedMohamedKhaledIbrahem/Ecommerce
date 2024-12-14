package com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled

interface IIsDarkModeEnableUseCase {
    suspend operator fun invoke(): Boolean
}