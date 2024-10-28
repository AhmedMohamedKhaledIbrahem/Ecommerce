package com.example.ecommerce.features.productsearch.data.datasources

import com.example.ecommerce.core.constants.apiKey
import com.example.ecommerce.core.constants.headerHost
import com.example.ecommerce.core.constants.headerKey
import com.example.ecommerce.core.constants.hostKey
import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProductSearchApi {
    @GET("products-by-category")
    suspend fun getProductByCategory(
        @Query("category_id") categoryId: String,
        @Query("page") page:Int ,
        @Query("country") country:String = "EG",
        @Header(headerKey) key:String = apiKey,
        @Header(headerHost) host:String = hostKey
    ):Response<List<ProductSearchModel>>
}