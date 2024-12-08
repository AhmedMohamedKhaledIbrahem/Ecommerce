package com.example.ecommerce.features.address.domain.usecases.checkupdateaddress

import com.example.ecommerce.core.data.entities.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import javax.inject.Inject

class CheckUpdateAddressUseCaseUseCase @Inject constructor(
    val repository: AddressRepository
) : ICheckUpdateAddressUseCase {
    override suspend fun invoke(): CustomerAddressEntity {
      return  repository.checkUpdateAddress()
    }
}