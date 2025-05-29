package com.example.ecommerce.features.address.domain.usecases.selectaddress

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class SelectAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) : ISelectAddressUseCase {
    override suspend fun invoke(customerId: Int) {
        repository.selectAddress(customerId = customerId)
    }
}