package com.example.ecommerce.features.address.module

import com.example.ecommerce.features.address.data.datasources.AddressApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideAddressApi(retrofit: Retrofit):AddressApi{
        return retrofit.create(AddressApi::class.java)
    }
}