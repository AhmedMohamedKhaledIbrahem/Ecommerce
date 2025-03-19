package com.example.ecommerce.features.address.data.repositories

import android.content.Context
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.address.data.datasources.localdatasource.AddressLocalDataSource
import com.example.ecommerce.features.address.data.datasources.remotedatasource.AddressRemoteDataSource
import com.example.ecommerce.features.address.data.mapper.AddressMapper
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddressRepositoryImp @Inject constructor(
    private val remoteDataSource: AddressRemoteDataSource,
    private val localDataSource: AddressLocalDataSource,
    private val internetConnectionChecker: InternetConnectionChecker,
    @ApplicationContext private val context: Context
) : AddressRepository {
    override suspend fun updateAddress(
        id: Int,
        customerAddressParams: AddressRequestEntity
    ): UpdateAddressResponseEntity {
        return try {
            if (!internetConnectionChecker.hasConnection()) {
                throw Failures.ConnectionFailure(context.getString(R.string.no_internet_connection))
            }
            val updateAddressParams = AddressMapper.mapToModel(customerAddressParams)
            try {
                localDataSource.updateAddress(id = id, updateAddressParams)
            } catch (e: FailureException) {
                throw Failures.CacheFailure(
                    e.localizedMessage ?: context.getString(R.string.unknown_error)
                )
            }
            val updateAddressResponseModel = remoteDataSource.updateAddress(updateAddressParams)
            AddressMapper.mapToEntity(updateAddressResponseModel)
        } catch (e: FailureException) {
            throw Failures.ServerFailure(
                e.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }

    }

    override suspend fun getAddress(): List<CustomerAddressEntity> {
        return try {
            if (!internetConnectionChecker.hasConnection()) {
                throw Failures.ConnectionFailure(context.getString(R.string.no_internet_connection))
            }
            if (!localDataSource.isAddressEmpty()) {
                localDataSource.getAddress()
            } else {
                val addressDataResponseModel = remoteDataSource.getAddress()
                val addressRequestModel = AddressMapper
                    .mapAddressResponseModelToAddressRequestModel(
                        addressDataResponseModel
                    )
                localDataSource.insertAddress(addressRequestModel)
                localDataSource.getAddress()
            }
            localDataSource.getAddress()
        } catch (e: FailureException) {
            throw Failures.CacheFailure(
                e.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }

    }

    override suspend fun insertAddress(customerAddressParams: AddressRequestEntity) {
        try {
            if (!internetConnectionChecker.hasConnection()) {
                throw Failures.ConnectionFailure(context.getString(R.string.no_internet_connection))
            }
            val addressRequestModel = AddressMapper.mapToModel(customerAddressParams)
            try {
                localDataSource.insertAddress(addressRequestModel)
            } catch (e: FailureException) {
                throw Failures.CacheFailure(
                    e.localizedMessage ?: context.getString(R.string.unknown_error)
                )
            }
            remoteDataSource.updateAddress(addressRequestModel)

        } catch (e: FailureException) {
            throw Failures.ServerFailure(
                e.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }

    }

    override suspend fun deleteAddress() {
        try {
            localDataSource.deleteAddress()
        } catch (e: FailureException) {
            throw Failures.CacheFailure(
                e.localizedMessage ?: context.getString(R.string.unknown_error)
            )
        }

    }


}