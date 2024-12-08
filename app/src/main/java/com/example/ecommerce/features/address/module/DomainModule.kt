package com.example.ecommerce.features.address.module

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.checkupdateaddress.CheckUpdateAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.checkupdateaddress.ICheckUpdateAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddressbyid.GetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddressbyid.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.IUpdateAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.UpdateAddressUseCase
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
    fun provideGetAddressUseCase(repository: AddressRepository): IGetAddressUseCase {
        return GetAddressUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUpdateAddressUseCase(repository: AddressRepository): IUpdateAddressUseCase {
        return UpdateAddressUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideCheckUpdateAddressUseCase(repository: AddressRepository): ICheckUpdateAddressUseCase {
        return CheckUpdateAddressUseCase(repository = repository)
    }

}