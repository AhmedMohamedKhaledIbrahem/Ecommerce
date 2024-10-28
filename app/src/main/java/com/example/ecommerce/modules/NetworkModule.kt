package com.example.ecommerce.modules

import android.content.Context
import com.example.ecommerce.core.constants.urlBase
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.core.network.interceptor.AuthenticationInterceptor
import com.example.ecommerce.features.authentication.data.datasources.AuthenticationApi
import com.example.ecommerce.features.productsearch.data.datasources.ProductSearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authenticationInterceptor: AuthenticationInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .build()
    }
    //api
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl(urlBase)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideProductSearchApi(retrofit: Retrofit):ProductSearchApi{
        return  retrofit.create(ProductSearchApi::class.java)
    }
    @Provides
    @Singleton
    fun provideAuthenticationApi(retrofit: Retrofit):AuthenticationApi{
        return  retrofit.create(AuthenticationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInternetConnectionChecker(context: Context): InternetConnectionChecker {
        return InternetConnectionChecker(context)
    }




}