package com.example.ecommerce.features.authentication.modules

import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode.CheckVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode.ICheckVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.login.ILoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.login.LoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.logout.ILogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.logout.LogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.reestpassword.IResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.reestpassword.ResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.SendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.ISignUpUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.SignUpUseCase
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
    fun provideLoginUseCase(authenticationRepository: AuthenticationRepository): ILoginUseCase {
        return LoginUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authenticationRepository: AuthenticationRepository): ISignUpUseCase {
        return SignUpUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(authenticationRepository: AuthenticationRepository): IResetPasswordUseCase {
        return ResetPasswordUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authenticationRepository: AuthenticationRepository): ILogoutUseCase {
        return LogoutUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideSendVerificationCode(authenticationRepository: AuthenticationRepository)
            : ISendVerificationCodeUseCase {
        return SendVerificationCodeUseCase(repository = authenticationRepository)
    }

    @Provides
    @Singleton
    fun provideCheckVerificationCode(authenticationRepository: AuthenticationRepository)
            : ICheckVerificationCodeUseCase {
        return CheckVerificationCodeUseCase(repository = authenticationRepository)
    }
}