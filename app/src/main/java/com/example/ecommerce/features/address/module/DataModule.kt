package com.example.ecommerce.features.address.module

import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.address.data.datasources.AddressApi
import com.example.ecommerce.features.address.data.datasources.localdatasource.AddressLocalDataSource
import com.example.ecommerce.features.address.data.datasources.localdatasource.AddressLocalDataSourceImp
import com.example.ecommerce.features.address.data.datasources.remotedatasource.AddressRemoteDataSource
import com.example.ecommerce.features.address.data.datasources.remotedatasource.AddressRemoteDataSourceImp
import com.example.ecommerce.features.address.data.repositories.AddressRepositoryImp
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    @Singleton
    fun provideAddressLocalDataSource(dao: AddressDao): AddressLocalDataSource {
        return AddressLocalDataSourceImp(dao = dao)
    }

    @Provides
    @Singleton
    fun provideAddressRemoteDataSource(api: AddressApi): AddressRemoteDataSource {
        return AddressRemoteDataSourceImp(api = api)
    }

    @Provides
    @Singleton
    fun provideAddressRepository(
        localDataSource: AddressLocalDataSource,
        remoteDataSource: AddressRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): AddressRepository {
        return AddressRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }
}