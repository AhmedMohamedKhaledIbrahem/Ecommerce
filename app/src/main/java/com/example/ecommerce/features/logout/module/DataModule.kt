package com.example.ecommerce.features.logout.module

import com.example.ecommerce.core.database.data.dao.logout.LogoutDao
import com.example.ecommerce.core.manager.address.AddressManager
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.manager.token.TokenManager
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.logout.data.repository.LogoutRepositoryImp
import com.example.ecommerce.features.logout.data.source.LogoutApi
import com.example.ecommerce.features.logout.data.source.local.LogoutLocalDataSource
import com.example.ecommerce.features.logout.data.source.local.LogoutLocalDataSourceImp
import com.example.ecommerce.features.logout.data.source.remote.LogoutRemoteDataSource
import com.example.ecommerce.features.logout.data.source.remote.LogoutRemoteDataSourceImp
import com.example.ecommerce.features.logout.domain.repository.LogoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideLogoutLocalDataSource(
        dao: LogoutDao,
        customerManager: CustomerManager,
        productHandler: ProductHandler,
        addressManager: AddressManager,
        tokenManager: TokenManager

    ): LogoutLocalDataSource {
        return LogoutLocalDataSourceImp(
            dao = dao,
            customerManager = customerManager,
            productHandler = productHandler,
            addressManager = addressManager,
            tokenManager = tokenManager
        )
    }

    @Provides
    @Singleton
    fun provideLogoutRemoteDataSource(api: LogoutApi): LogoutRemoteDataSource {
        return LogoutRemoteDataSourceImp(api = api)
    }

    @Provides
    @Singleton
    fun provideLogoutRepository(
        localDataSource: LogoutLocalDataSource,
        remoteDataSource: LogoutRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): LogoutRepository {
        return LogoutRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }
}