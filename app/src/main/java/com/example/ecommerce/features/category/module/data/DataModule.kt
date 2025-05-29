package com.example.ecommerce.features.category.module.data

import com.example.ecommerce.core.database.data.dao.category.CategoryDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.category.data.data_source.CategoryApi
import com.example.ecommerce.features.category.data.data_source.local.CategoryLocalDataSource
import com.example.ecommerce.features.category.data.data_source.local.CategoryLocalDataSourceImp
import com.example.ecommerce.features.category.data.data_source.remote.CategoryRemoteDataSource
import com.example.ecommerce.features.category.data.data_source.remote.CategoryRemoteDataSourceImp
import com.example.ecommerce.features.category.data.repository.CategoryRepositoryImp
import com.example.ecommerce.features.category.domain.repository.CategoryRepository
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
    fun provideCategoryLocalDataSource(categoryDao: CategoryDao): CategoryLocalDataSource {
        return CategoryLocalDataSourceImp(categoryDao = categoryDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRemoteDataSource(categoryApi: CategoryApi): CategoryRemoteDataSource {
        return CategoryRemoteDataSourceImp(categoryApi = categoryApi)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        localDataSource: CategoryLocalDataSource,
        remoteDataSource: CategoryRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): CategoryRepository {
        return CategoryRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            connectionChecker = internetConnectionChecker
        )
    }
}