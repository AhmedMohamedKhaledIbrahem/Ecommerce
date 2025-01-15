package com.example.ecommerce.features.address.domain.usecases.getaddressbyid

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(val repository: AddressRepository) :
    IGetAddressUseCase {
    override suspend fun invoke(id:Int): CustomerAddressEntity {
        return repository.getAddressById(id)
    }

}