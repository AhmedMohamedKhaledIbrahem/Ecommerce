package com.example.ecommerce.features.orders.modules

import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.orders.data.data_source.OrderApi
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
    fun provideOrderRemoteDataSource(orderApi: OrderApi): OrderRemoteDataSource {
        return OrderRemoteDataSourceImp(orderApi = orderApi)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        remoteDataSource: OrderRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): OrderRepository {
        return OrderRepositoryImp(
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }

}