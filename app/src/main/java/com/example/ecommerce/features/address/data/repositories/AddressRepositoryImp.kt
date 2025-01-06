package com.example.ecommerce.features.address.data.repositories

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.address.data.datasources.localdatasource.AddressLocalDataSource
import com.example.ecommerce.features.address.data.datasources.remotedatasource.AddressRemoteDataSource
import com.example.ecommerce.features.address.data.mapper.AddressMapper
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class AddressRepositoryImp @Inject constructor(
    private val remoteDataSource: AddressRemoteDataSource,
    private val localDataSource: AddressLocalDataSource,
    private val internetConnectionChecker: InternetConnectionChecker
) : AddressRepository {


    override suspend fun updateAddress(customerAddressParams: AddressRequestEntity) {
        try {
            if (internetConnectionChecker.hasConnection()) {
                val updateAddressParams = AddressMapper.mapToModel(customerAddressParams)
                val updateAddressRemote = remoteDataSource.updateAddress(updateAddressParams)
                localDataSource.updateAddress(updateAddressParams = updateAddressRemote)
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: " Unknown Error")
        }
    }

    override suspend fun getAddressById(id: Int): CustomerAddressEntity {
        return try {
            if (internetConnectionChecker.hasConnection()) {
                localDataSource.checkAddressEntityById(id)?.let {
                    localDataSource.getAddressById(id = id)
                } ?: run {
                    val remoteGetAddress = remoteDataSource.getAddress()
                    localDataSource.updateAddress(updateAddressParams = remoteGetAddress)
                    localDataSource.getAddressById(id = id)
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.CacheFailure(e.message ?: " Unknown Error")
        }
    }

    override suspend fun checkUpdateAddress(): CustomerAddressEntity {
        return try {
            if (internetConnectionChecker.hasConnection()) {

                remoteDataSource.checkUpdateAddress()
                    .takeIf { updateResponse -> updateResponse.isUpdate }?.let { updateResponse ->
                        val remoteGetAddress = remoteDataSource.getAddress()
                        try {
                            localDataSource.updateAddress(updateAddressParams = remoteGetAddress)
                            localDataSource.getAddressById(id = updateResponse.id)
                        } catch (e: FailureException) {
                            throw Failures.CacheFailure(e.message ?: " Unknown Error")
                        }
                    } ?: run {
                    throw Failures.ServerFailure("there's not update")
                }
            } else {
                throw Failures.ConnectionFailure("No Internet Connection")
            }
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: "Unknown Error")

        }
    }

}