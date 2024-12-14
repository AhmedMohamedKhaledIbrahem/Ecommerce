package com.example.ecommerce.features.preferences.domain.usecase.setlanguage

interface ISetLanguageUseCase {
    suspend operator fun invoke(languageCode: String)
}