package com.example.ecommerce.modules

import android.content.Context
import com.example.ecommerce.core.constants.urlBase
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.core.network.checknetwork.InternetConnectionCheckerImp
import com.example.ecommerce.core.network.interceptor.AuthenticationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkCoreModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(authenticationInterceptor: AuthenticationInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlBase)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideInternetConnectionChecker(context: Context): InternetConnectionChecker {
        return InternetConnectionCheckerImp(context)
    }


}