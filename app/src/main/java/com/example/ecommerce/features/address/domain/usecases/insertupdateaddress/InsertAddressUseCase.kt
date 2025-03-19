package com.example.ecommerce.features.address.domain.usecases.insertupdateaddress

import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class InsertAddressUseCase @Inject constructor(
    val repository: AddressRepository
) : IInsertAddressUseCase {
    override suspend fun invoke(customerAddressParams: AddressRequestEntity) {
        return repository.insertAddress(customerAddressParams = customerAddressParams)
    }
}