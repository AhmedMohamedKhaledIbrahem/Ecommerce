package com.example.ecommerce.features.address.module

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.deleteaddress.DeleteAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.deleteaddress.IDeleteAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddress.GetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddress.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.IInsertAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.InsertAddressUseCase
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
    fun provideCheckUpdateAddressUseCase(repository: AddressRepository): IInsertAddressUseCase {
        return InsertAddressUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideDeleteAddressUseCase(repository: AddressRepository): IDeleteAddressUseCase {
        return DeleteAddressUseCase(repository = repository)
    }


}