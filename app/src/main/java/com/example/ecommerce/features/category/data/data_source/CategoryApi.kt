package com.example.ecommerce.features.category.data.data_source

import com.example.ecommerce.features.category.data.model.CategoryResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("wp-json/custom/v1/product-categories")
    suspend fun getProductCategories(): Response<CategoryResponseModel>

}