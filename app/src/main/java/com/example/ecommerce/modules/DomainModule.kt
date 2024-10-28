package com.example.ecommerce.modules

import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import com.example.ecommerce.features.authentication.domain.usecases.LoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.LogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.ResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.SignUpUseCase
import com.example.ecommerce.features.productsearch.domain.repositories.ProductSearchRepository
import com.example.ecommerce.features.productsearch.domain.usecases.GetProductsByCategory
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
    fun provideGetProductsByCategory(productSearchRepository: ProductSearchRepository): GetProductsByCategory {
        return GetProductsByCategory(productSearchRepository = productSearchRepository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authenticationRepository: AuthenticationRepository): LoginUseCase {
        return LoginUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authenticationRepository: AuthenticationRepository): SignUpUseCase {
        return SignUpUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(authenticationRepository: AuthenticationRepository): ResetPasswordUseCase {
        return ResetPasswordUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authenticationRepository: AuthenticationRepository):LogoutUseCase{
     return LogoutUseCase(repository = authenticationRepository)  
    }
}