package com.example.ecommerce.features.address.data.datasources.localdatasource

import android.util.Log
import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.mapper.CustomerAddressMapper
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressLocalDataSourceImp @Inject constructor(
    private val dao: AddressDao
) : AddressLocalDataSource {

    override suspend fun updateAddress(updateAddressParams: AddressResponseModel) {
        withContext(Dispatchers.IO) {
            try {
                val address = dao.getAddressById(updateAddressParams.userId)
                if (address != null) {
                    address.firstName = updateAddressParams.data?.billing?.firstName
                    address.lastName = updateAddressParams.data?.billing?.lastName
                    address.address = updateAddressParams.data?.billing?.address
                    address.email = updateAddressParams.data?.billing?.email
                    address.city = updateAddressParams.data?.billing?.city
                    address.state = updateAddressParams.data?.billing?.state
                    address.zipCode = updateAddressParams.data?.billing?.postCode
                    address.country = updateAddressParams.data?.billing?.country
                    address.phone = updateAddressParams.data?.billing?.phone
                    dao.updateAddress(address)

                } else {
                    val customerAddressEntity =
                        CustomerAddressMapper.mapToEntity(updateAddressParams)
                    dao.insertAddress(customerAddressEntity = customerAddressEntity)
                }
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }

    override suspend fun getAddressById(id: Int): CustomerAddressEntity {
        return withContext(Dispatchers.IO) {
            try {
                dao.getAddress(id)
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }


    override suspend fun checkAddressEntityById(id: Int): CustomerAddressEntity? {
        return withContext(Dispatchers.IO) {
            try {
                dao.getAddressById(id)
            } catch (e: Exception) {
                throw FailureException("${e.message}")
            }
        }
    }
}