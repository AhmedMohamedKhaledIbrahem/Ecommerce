package com.example.ecommerce.features.address.data.datasources.localdatasource

import android.content.Context
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.mapper.CustomerAddressMapper
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddressLocalDataSourceImp @Inject constructor(
    private val dao: AddressDao,
    @ApplicationContext private val context: Context
) : AddressLocalDataSource {

    override suspend fun updateAddress(id: Int, addressRequestParams: AddressRequestModel) {
        try {
            val isAddressEmpty = isAddressEmpty()
            when (isAddressEmpty) {
                true -> {
                    throw FailureException(context.getString(R.string.no_address_found))
                }

                false -> {
                    val addressRequestEntity = CustomerAddressMapper.mapToEntity(
                        id = id,
                        model = addressRequestParams
                    )
                    dao.updateAddress(addressRequestEntity)
                }
            }
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }

    }

    override suspend fun insertAddress(addressRequestParams: AddressRequestModel) {
        try {
            val customerAddressEntity =
                CustomerAddressMapper.mapToEntity(addressRequestParams)
            dao.insertAddress(customerAddressEntity = customerAddressEntity)
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }

    }

    override suspend fun getAddress(): List<CustomerAddressEntity> {
        return try {
            dao.getAddress()
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }

    }

    override suspend fun isAddressEmpty(): Boolean {
        return try {
            dao.getCount() == 0
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }
    }

    override suspend fun deleteAllAddress() {
        try {
            dao.deleteAllAddress()
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }
    }

    override suspend fun deleteAddress(customerAddressEntity: CustomerAddressEntity) {
        try {
            dao.deleteAddress(customerAddressEntity = customerAddressEntity)
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }
    }

    override suspend fun getSelectAddress(customerId: Int): CustomerAddressEntity {
        return try {
            dao.getSelectAddress(customerId = customerId)
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }
    }

    override suspend fun unSelectAddress(customerId: Int) {
        try {
            dao.unSelectAddress(customerId = customerId)
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }
    }

    override suspend fun selectAddress(customerId: Int) {
        try {
            dao.selectAddress(customerId = customerId)
        } catch (e: Exception) {
            throw FailureException(e.localizedMessage ?: context.getString(R.string.unknown_error))
        }
    }


}