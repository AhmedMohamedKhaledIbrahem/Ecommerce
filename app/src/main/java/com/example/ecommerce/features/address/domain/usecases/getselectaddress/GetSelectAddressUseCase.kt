package com.example.ecommerce.features.address.domain.usecases.getselectaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class GetSelectAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) : IGetSelectAddressUseCase {
    override suspend fun invoke(customerId: Int): CustomerAddressEntity {
        return repository.getSelectAddress(customerId = customerId)
    }
}