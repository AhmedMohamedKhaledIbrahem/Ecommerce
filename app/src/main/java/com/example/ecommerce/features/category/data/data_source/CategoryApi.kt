package com.example.ecommerce.features.category.data.data_source

import com.example.ecommerce.core.constants.GET_PRODUCT_CATEGORIES_END_POINT
import com.example.ecommerce.features.category.data.model.CategoryResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET(GET_PRODUCT_CATEGORIES_END_POINT)
    suspend fun getProductCategories(): Response<CategoryResponseModel>

}