package com.example.ecommerce.features.product.data.datasource

import com.example.ecommerce.features.product.data.model.EcommerceResponseModel
import com.example.ecommerce.features.product.data.model.ProductResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {
    @GET("wp-json/cocart/v2/products")
    suspend fun getProducts(): Response<EcommerceResponseModel>
}