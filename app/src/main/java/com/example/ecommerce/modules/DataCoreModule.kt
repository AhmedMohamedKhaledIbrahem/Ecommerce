package com.example.ecommerce.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.ecommerce.core.appdatabase.AppDatabase
import com.example.ecommerce.core.data.dao.UserDao
import com.example.ecommerce.core.tokenmanager.TokenManager
import com.example.ecommerce.core.tokenmanager.TokenManagerImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataCoreModule {

    @Provides
    @Singleton
    fun provideDatabase(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ecommerce_database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
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
}