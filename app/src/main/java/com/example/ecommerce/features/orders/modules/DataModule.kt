package com.example.ecommerce.features.orders.modules

import com.example.ecommerce.core.customer.CustomerManager
import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.orders.OrderItemDao
import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.orders.data.data_source.OrderApi
import com.example.ecommerce.features.orders.data.data_source.local.OrderLocalDataSource
import com.example.ecommerce.features.orders.data.data_source.local.OrderLocalDataSourceImp
import com.example.ecommerce.features.orders.data.data_source.remote.OrderRemoteDataSource
import com.example.ecommerce.features.orders.data.data_source.remote.OrderRemoteDataSourceImp
import com.example.ecommerce.features.orders.data.repository.OrderRepositoryImp
import com.example.ecommerce.features.orders.domain.repository.OrderRepository
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
    fun provideOrderRemoteDataSource(
        orderApi: OrderApi,
        customerManager: CustomerManager
    ): OrderRemoteDataSource {
        return OrderRemoteDataSourceImp(orderApi = orderApi, customerManager = customerManager)
    }

    @Provides
    @Singleton
    fun provideOrderLocalDataSource(
        orderItemDao: OrderItemDao,
        orderTagDao: OrderTagDao,
        imageDao: ImageDao
    ): OrderLocalDataSource {
        return OrderLocalDataSourceImp(
            orderItemDao = orderItemDao,
            orderTagDao = orderTagDao,
            imageDao = imageDao,
        )
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        remoteDataSource: OrderRemoteDataSource,
        localDataSource: OrderLocalDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): OrderRepository {
        return OrderRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            internetConnectionChecker = internetConnectionChecker,
        )
    }

}