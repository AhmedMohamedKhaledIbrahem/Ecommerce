package com.example.ecommerce.features.address.domain.usecases.deletealladdress

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class DeleteAllAddressUseCase @Inject constructor(
    private val repository: AddressRepository
) : IDeleteAllAddressUseCase {
    override suspend fun invoke() {
        return repository.deleteAllAddress()
    }
}