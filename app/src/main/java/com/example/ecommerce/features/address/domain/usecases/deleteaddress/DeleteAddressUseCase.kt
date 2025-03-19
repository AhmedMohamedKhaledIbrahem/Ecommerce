package com.example.ecommerce.features.address.domain.usecases.deleteaddress

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) : IDeleteAddressUseCase {
    override suspend fun invoke() {
        return repository.deleteAddress()
    }
}