package com.example.ecommerce.features.address.module

import android.content.Context
import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.manager.address.AddressManager
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Provides
    @Singleton
    fun provideAddressLocalDataSource(
        dao: AddressDao,
        @ApplicationContext context: Context
    ): AddressLocalDataSource {
        return AddressLocalDataSourceImp(
            dao = dao,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideAddressRemoteDataSource(
        api: AddressApi,
        @ApplicationContext context: Context
    ): AddressRemoteDataSource {
        return AddressRemoteDataSourceImp(api = api, context = context)
    }

    @Provides
    @Singleton
    fun provideAddressRepository(
        localDataSource: AddressLocalDataSource,
        remoteDataSource: AddressRemoteDataSource,
       addressManager: AddressManager,
        internetConnectionChecker: InternetConnectionChecker,
        @ApplicationContext context: Context
    ): AddressRepository {
        return AddressRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            addressManager = addressManager,
            internetConnectionChecker = internetConnectionChecker,
            context = context
        )
    }
}