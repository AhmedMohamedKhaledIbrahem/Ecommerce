package com.example.ecommerce.features.notification.module

import com.example.ecommerce.features.notification.data.source.remote.FcmApi
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
    fun provideFcmTokenApi(retrofit: Retrofit): FcmApi {
        return retrofit.create(FcmApi::class.java)
    }
}