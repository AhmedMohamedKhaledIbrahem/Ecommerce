package com.example.ecommerce.features.address.domain.usecases.unselectaddress

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class UnSelectAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) : IUnSelectAddressUseCase {
    override suspend fun invoke(customerId: Int) {
        repository.unSelectAddress(customerId = customerId)
    }
}