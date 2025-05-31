package com.example.ecommerce.features.logout.module

import com.example.ecommerce.features.logout.domain.repository.LogoutRepository
import com.example.ecommerce.features.logout.domain.use_case.LogoutUseCase
import com.example.ecommerce.features.logout.domain.use_case.LogoutUseCaseImp
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
    fun provideLogoutUseCase(repository: LogoutRepository): LogoutUseCase {
        return LogoutUseCaseImp(repository = repository)
    }
}