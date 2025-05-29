package com.example.ecommerce.features.address.domain.usecases.deleteaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) : IDeleteAddressUseCase {
    override suspend fun invoke(customerAddressEntity: CustomerAddressEntity) {
        repository.deleteAddress(customerAddressEntity = customerAddressEntity)
    }
}