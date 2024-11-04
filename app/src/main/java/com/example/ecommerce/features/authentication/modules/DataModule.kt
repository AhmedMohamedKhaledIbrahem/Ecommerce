package com.example.ecommerce.features.authentication.modules

import com.example.ecommerce.core.data.dao.UserDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.core.tokenmanager.TokenManager
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationLocalDataSourceImp
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSource
import com.example.ecommerce.features.authentication.data.datasources.localdatasource.AuthenticationSharedPreferencesDataSourceImp
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSource
import com.example.ecommerce.features.authentication.data.datasources.remotedatasource.AuthenticationRemoteDataSourceImp
import com.example.ecommerce.features.authentication.data.repositories.AuthenticationRepositoryImp
import com.example.ecommerce.features.authentication.domain.repositories.AuthenticationRepository
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
            preferences = preferences,
            networkInfo = internetConnectionChecker
        )
    }


}