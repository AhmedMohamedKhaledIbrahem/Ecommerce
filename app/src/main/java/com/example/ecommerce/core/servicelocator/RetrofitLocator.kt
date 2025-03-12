package com.example.ecommerce.core.servicelocator

import com.example.ecommerce.core.constants.urlBase
import com.example.ecommerce.features.notification.data.source.remote.FcmApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitLocator {

    private fun provideFcmApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val fcmApi: FcmApi = provideFcmApiRetrofit().create(FcmApi::class.java)
}