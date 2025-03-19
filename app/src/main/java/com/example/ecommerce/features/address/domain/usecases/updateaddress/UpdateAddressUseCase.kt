package com.example.ecommerce.features.address.domain.usecases.updateaddress

import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(val repository: AddressRepository) :
    IUpdateAddressUseCase {
    override suspend fun invoke(id: Int, customerAddressParams: AddressRequestEntity) {
        repository.updateAddress(id = id, customerAddressParams = customerAddressParams)
    }


}