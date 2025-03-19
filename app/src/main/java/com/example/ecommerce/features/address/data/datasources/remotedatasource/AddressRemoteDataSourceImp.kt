package com.example.ecommerce.features.address.data.datasources.remotedatasource

import android.content.Context
import com.example.ecommerce.R
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.address.data.datasources.AddressApi
import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class AddressRemoteDataSourceImp @Inject constructor(
    private val api: AddressApi,
    @ApplicationContext private val context: Context
) : AddressRemoteDataSource {
    override suspend fun updateAddress(updateAddressParams: AddressRequestModel): UpdateAddressResponseModel {
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
                throw FailureException(
                    e.localizedMessage ?: context.getString(R.string.unknown_error)
                )
            }
        }
    }

    override suspend fun getAddress(): AddressDataResponseModel {
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
                throw FailureException(
                    e.localizedMessage ?: context.getString(R.string.unknown_error)
                )
            }
        }
    }


}