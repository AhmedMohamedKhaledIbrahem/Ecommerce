package com.example.ecommerce.features.product.modules

import com.example.ecommerce.core.database.data.dao.category.CategoryDao
import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.product.ProductCategoryCrossRefDao
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.features.product.data.datasource.ProductApi
import com.example.ecommerce.features.product.data.datasource.localdatasource.ProductLocalDataSource
import com.example.ecommerce.features.product.data.datasource.localdatasource.ProductLocalDataSourceImp
import com.example.ecommerce.features.product.data.datasource.localdatasource.ProductPagingSource
import com.example.ecommerce.features.product.data.datasource.localdatasource.ProductSearchPagingSource
import com.example.ecommerce.features.product.data.datasource.remotedatasource.ProductRemoteDataSource
import com.example.ecommerce.features.product.data.datasource.remotedatasource.ProductRemoteDataSourceImp
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
    fun provideProductLocalDataSource(
        productDao: ProductDao,
        imageDao: ImageDao,
        categoryDao: CategoryDao,
        productCategoryCrossRefDao: ProductCategoryCrossRefDao
    ): ProductLocalDataSource {
        return ProductLocalDataSourceImp(
            productDao = productDao,
            imageDao = imageDao,
            categoryDao = categoryDao,
            productCategoryDao = productCategoryCrossRefDao
        )
    }

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(productApi: ProductApi): ProductRemoteDataSource {
        return ProductRemoteDataSourceImp(productApi = productApi)
    }

    @Provides
    @Singleton
    fun provideProductPagingSource(productDao: ProductDao): ProductPagingSource {
        return ProductPagingSource(productDao = productDao)
    }
    @Provides
    @Singleton
    fun provideProductSearchPagingSource(productDao: ProductDao , query: String): ProductSearchPagingSource {
        return ProductSearchPagingSource(productDao = productDao , query)
    }
}