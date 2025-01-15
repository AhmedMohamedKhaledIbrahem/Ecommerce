package com.example.ecommerce.features.cart.modules

import com.example.ecommerce.core.database.data.dao.cart.CartDao
import com.example.ecommerce.core.database.data.dao.cart.ItemCartDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.cart.data.data_soruce.CartApi
import com.example.ecommerce.features.cart.data.data_soruce.local.CartLocalDataSource
import com.example.ecommerce.features.cart.data.data_soruce.local.CartLocalDataSourceImp
import com.example.ecommerce.features.cart.data.data_soruce.remote.CartRemoteDataSource
import com.example.ecommerce.features.cart.data.data_soruce.remote.CartRemoteDataSourceImp
import com.example.ecommerce.features.cart.data.repository.CartRepositoryImp
import com.example.ecommerce.features.cart.domain.repository.CartRepository
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
    fun provideCartLocalDataSource(
        cartDao: CartDao,
        itemCartDao: ItemCartDao
    ): CartLocalDataSource {
        return CartLocalDataSourceImp(
            cartDao = cartDao,
            itemCartDao = itemCartDao,
        )
    }

    @Provides
    @Singleton
    fun provideCartRemoteDataSource(cartApi: CartApi): CartRemoteDataSource {
        return CartRemoteDataSourceImp(cartApi = cartApi)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        remoteDataSource: CartRemoteDataSource,
        localDataSource: CartLocalDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): CartRepository {
        return CartRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }
}