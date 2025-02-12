package com.example.ecommerce.features.orders.data.data_source.remote

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.orders.data.data_source.OrderApi
import com.example.ecommerce.features.orders.data.models.OrderRequestModel
import com.example.ecommerce.features.orders.data.models.OrderResponseModel
import org.json.JSONObject
import javax.inject.Inject

class OrderRemoteDataSourceImp @Inject constructor(
    private val orderApi: OrderApi
) : OrderRemoteDataSource {
    override suspend fun createOrder(orderRequestModel: OrderRequestModel): OrderResponseModel {
        return try {
            val response = orderApi.createOrder(orderRequestModel)
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