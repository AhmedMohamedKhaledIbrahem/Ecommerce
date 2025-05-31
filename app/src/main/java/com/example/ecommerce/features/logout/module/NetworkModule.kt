package com.example.ecommerce.features.logout.module

import com.example.ecommerce.features.logout.data.source.LogoutApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLogoutApi(retrofit: Retrofit): LogoutApi {
        return retrofit.create(LogoutApi::class.java)
    }
}