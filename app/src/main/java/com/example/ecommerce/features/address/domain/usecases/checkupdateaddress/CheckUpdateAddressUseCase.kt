package com.example.ecommerce.features.address.domain.usecases.checkupdateaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class CheckUpdateAddressUseCase @Inject constructor(
    val repository: AddressRepository
) : ICheckUpdateAddressUseCase {
    override suspend fun invoke(): CustomerAddressEntity {
        return repository.checkUpdateAddress()
    }
}