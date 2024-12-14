package com.example.ecommerce.features.preferences.modules

import android.content.SharedPreferences
import com.example.ecommerce.features.preferences.data.datasource.localdatasource.PreferencesLocalDataSource
import com.example.ecommerce.features.preferences.data.datasource.localdatasource.PreferencesLocalDataSourceImp
import com.example.ecommerce.features.preferences.data.repository.PreferencesRepositoryImp
import com.example.ecommerce.features.preferences.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providePreferencesLocalDataSource(sharedPreferences: SharedPreferences): PreferencesLocalDataSource {
        return PreferencesLocalDataSourceImp(sharedPreferences = sharedPreferences)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(localDataSource: PreferencesLocalDataSource): PreferencesRepository {
        return PreferencesRepositoryImp(localDataSource = localDataSource)
    }
}