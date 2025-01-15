package com.example.ecommerce.features.cart.data.data_soruce.remote

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.cart.data.data_soruce.CartApi
import com.example.ecommerce.features.cart.data.models.AddItemRequestModel
import com.example.ecommerce.features.cart.data.models.CartResponseModel
import org.json.JSONObject
import javax.inject.Inject

class CartRemoteDataSourceImp @Inject constructor(
    private val cartApi: CartApi
) : CartRemoteDataSource {
    override suspend fun getCart(): CartResponseModel {
        return try {
            val response = cartApi.getCart()
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

    override suspend fun addItemCart(addItemRequestModel: AddItemRequestModel): CartResponseModel {
        return try {
            val response = cartApi.addItemToCart(request = addItemRequestModel)
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

    override suspend fun deleteItemFromCard(keyItem: String) {
        try {
            cartApi.deleteItemFromCart(keyItem = keyItem)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }
}