package com.example.ecommerce.features.preferences.domain.usecase.getlanguage

interface IGetLanguageUseCase {
    suspend operator fun invoke(): String
}