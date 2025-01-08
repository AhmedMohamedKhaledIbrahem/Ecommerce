package com.example.ecommerce.features.address.data.datasources.remotedatasource

import android.util.Log
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.address.data.datasources.AddressApi
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class AddressRemoteDataSourceImp @Inject constructor(
    private val api: AddressApi
) : AddressRemoteDataSource {
    override suspend fun updateAddress(updateAddressParams: AddressRequestModel): AddressResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateAddress(updateAddressParams = updateAddressParams)
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

    override suspend fun getAddress(): AddressResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAddress()
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

    override suspend fun checkUpdateAddress(): CheckUpdateAddressResponseModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.checkUpdateAddress()
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
}