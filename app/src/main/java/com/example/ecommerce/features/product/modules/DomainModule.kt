package com.example.ecommerce.features.product.modules

import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.product.data.datasource.localdatasource.ProductLocalDataSource
import com.example.ecommerce.features.product.data.datasource.remotedatasource.ProductRemoteDataSource
import com.example.ecommerce.features.product.data.repository.ProductRepositoryImp
import com.example.ecommerce.features.product.domain.repository.ProductRepository
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
    fun provideProductRepository(
        productLocalDataSource: ProductLocalDataSource,
        productRemoteDataSource: ProductRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker,
    ): ProductRepository {
        return ProductRepositoryImp(
            productLocalDataSource = productLocalDataSource,
            productRemoteDataSource = productRemoteDataSource,
            checkInternetConnection = internetConnectionChecker
        )
    }
}