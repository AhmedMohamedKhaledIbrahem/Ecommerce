package com.example.ecommerce.features.product.data.datasource.remotedatasource

import android.util.Log
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.features.product.data.datasource.ProductApi
import com.example.ecommerce.features.product.data.model.EcommerceResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class ProductRemoteDataSourceImp @Inject constructor(
    private val productApi: ProductApi
) : ProductRemoteDataSource {
    override suspend fun getProducts(): EcommerceResponseModel {
        return try {
                val response = productApi.getProducts()
                if (response.isSuccessful) {
                    Log.e("responseBody", "${response.body()?.products?.size ?: 0}")
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