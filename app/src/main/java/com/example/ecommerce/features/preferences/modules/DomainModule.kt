package com.example.ecommerce.features.preferences.modules

import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import com.example.ecommerce.features.preferences.domain.usecase.getlanguage.GetLanguageUseCase
import com.example.ecommerce.features.preferences.domain.usecase.getlanguage.IGetLanguageUseCase
import com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled.IIsDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.isdarkmodeenabled.IsDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable.ISetDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setdarkmodeenable.SetDarkModeEnableUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setlanguage.ISetLanguageUseCase
import com.example.ecommerce.features.preferences.domain.usecase.setlanguage.SetLanguageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideGetLanguageUseCase(repository: PreferencesRepository): IGetLanguageUseCase {
        return GetLanguageUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSetLanguageUseCase(repository: PreferencesRepository): ISetLanguageUseCase {
        return SetLanguageUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideIsDarkModeEnableUseCase(repository: PreferencesRepository): IIsDarkModeEnableUseCase {
        return IsDarkModeEnableUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSetDarkModeEnableUseCase(repository: PreferencesRepository): ISetDarkModeEnableUseCase {
        return SetDarkModeEnableUseCase(repository = repository)
    }


}