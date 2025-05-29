package com.example.ecommerce.features.category.data.data_source.remote

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.category.data.data_source.CategoryApi
import com.example.ecommerce.features.category.data.model.CategoryResponseModel
import org.json.JSONObject
import javax.inject.Inject

interface CategoryRemoteDataSource {
    suspend fun fetchCategories(): CategoryResponseModel
}

class CategoryRemoteDataSourceImp @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRemoteDataSource {
    override suspend fun fetchCategories(): CategoryResponseModel {
        return try {
            val response = categoryApi.getProductCategories()
            if (response.isSuccessful) {
                response.body() ?: throw FailureException("Empty Response Body")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody?.let {
                    JSONObject(it).optString("message", "Unknown error")
                } ?: "Unknown error"
                throw FailureException(errorMessage)
            }
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }

}