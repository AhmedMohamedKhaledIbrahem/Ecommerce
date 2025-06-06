package com.example.ecommerce.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.example.ecommerce.EcommerceApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
   @Provides
   @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}