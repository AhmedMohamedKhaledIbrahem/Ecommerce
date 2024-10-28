package com.example.ecommerce.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.ecommerce.core.appdatabase.AppDatabase
import com.example.ecommerce.core.data.dao.UserDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.core.tokenmanager.TokenManager
import com.example.ecommerce.core.tokenmanager.TokenManagerImp
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSourceImp
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSourceImp
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSource
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSourceImp
import com.example.ecommerce.features.authentication.data.repositories.AuthenticationRepositoryImp
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
import com.example.ecommerce.features.productsearch.data.datasources.ProductSearchApi
import com.example.ecommerce.features.productsearch.data.datasources.ProductSearchRemoteDataSource
import com.example.ecommerce.features.productsearch.data.datasources.ProductSearchRemoteDataSourceImpl
import com.example.ecommerce.features.productsearch.data.repositories.ProductSearchRepositoryImpl
import com.example.ecommerce.features.productsearch.domain.repositories.ProductSearchRepository
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
    fun provideDatabase(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ecommerce_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideTokenManager(preferences: SharedPreferences): TokenManager {
        return TokenManagerImp(preferences = preferences)
    }

    @Provides
    @Singleton
    fun provideProductSearchRemoteDataSource(
        productSearchApi: ProductSearchApi
    ): ProductSearchRemoteDataSource {
        return ProductSearchRemoteDataSourceImpl(productSearchApi)
    }

    @Provides
    @Singleton
    fun provideProductSearchRepository(
        remoteDataSource: ProductSearchRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): ProductSearchRepository {
        return ProductSearchRepositoryImpl(
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }

    @Provides
    @Singleton
    fun provideAuthenticationLocalDataSource(dao: UserDao): AuthenticationLocalDataSource {
        return AuthenticationLocalDataSourceImp(dao = dao)
    }

    @Provides
    @Singleton
    fun provideAuthenticationSharedPreferencesDataSource(tokenManager: TokenManager):
            AuthenticationSharedPreferencesDataSource {
        return AuthenticationSharedPreferencesDataSourceImp(tokenManager = tokenManager)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRemoteDataSource(api: AuthenticationApi):
            AuthenticationRemoteDataSource {
        return AuthenticationRemoteDataSourceImp(api = api)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        localDataSource: AuthenticationLocalDataSource,
        remoteDataSource: AuthenticationRemoteDataSource,
        preferences: AuthenticationSharedPreferencesDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): AuthenticationRepository {
        return AuthenticationRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            preferences=preferences,
            networkInfo = internetConnectionChecker
        )
    }


}