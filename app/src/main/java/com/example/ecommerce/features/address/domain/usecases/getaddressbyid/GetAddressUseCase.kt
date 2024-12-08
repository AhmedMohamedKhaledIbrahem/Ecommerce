package com.example.ecommerce.features.address.domain.usecases.getaddress

import com.example.ecommerce.core.data.entities.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(val repository: AddressRepository) :
    IGetAddressUseCase {
    override suspend fun invoke(): CustomerAddressEntity {
        return repository.getAddress()
    }

}