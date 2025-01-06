package com.example.ecommerce.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.ecommerce.core.database.AppDatabase
import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.database.data.dao.category.CategoryDao
import com.example.ecommerce.core.database.data.dao.image.ImageDao
import com.example.ecommerce.core.database.data.dao.product.ProductCategoryCrossRefDao
import com.example.ecommerce.core.database.data.dao.product.ProductDao
import com.example.ecommerce.core.database.data.dao.user.UserDao
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
    fun provideAddressDao(database: AppDatabase): AddressDao {
        return database.addressDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideImageDao(database: AppDatabase): ImageDao {
        return database.imageDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideProductCategoryCrossRefDao(database: AppDatabase): ProductCategoryCrossRefDao {
        return database.productCategoryCrossRefDao()
    }

    @Provides
    @Singleton
    fun provideTokenManager(preferences: SharedPreferences): TokenManager {
        return TokenManagerImp(preferences = preferences)
    }
}