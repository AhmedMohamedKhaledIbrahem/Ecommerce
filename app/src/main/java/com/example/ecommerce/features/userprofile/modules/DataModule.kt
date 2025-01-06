package com.example.ecommerce.features.userprofile.modules

import com.example.ecommerce.core.database.data.dao.user.UserDao
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.userprofile.data.datasources.UserProfileApi
import com.example.ecommerce.features.userprofile.data.datasources.localdatasource.UserProfileLocalDataSource
import com.example.ecommerce.features.userprofile.data.datasources.localdatasource.UserProfileLocalDataSourceImp
import com.example.ecommerce.features.userprofile.data.datasources.remotedatasource.UserProfileRemoteDataSource
import com.example.ecommerce.features.userprofile.data.datasources.remotedatasource.UserProfileRemoteDataSourceImp
import com.example.ecommerce.features.userprofile.data.repositories.UserProfileRepositoryImp
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
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
    fun provideUserProfileLocalDataSource(dao: UserDao): UserProfileLocalDataSource {
        return UserProfileLocalDataSourceImp(dao = dao)
    }

    @Provides
    @Singleton
    fun provideUserProfileRemoteDataSource(
        api: UserProfileApi,
    ): UserProfileRemoteDataSource {
        return UserProfileRemoteDataSourceImp(
            api = api,
        )
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        localDataSource: UserProfileLocalDataSource,
        remoteDataSource: UserProfileRemoteDataSource,
        internetConnectionChecker: InternetConnectionChecker
    ): UserProfileRepository {
        return UserProfileRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }

}