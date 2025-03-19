package com.example.ecommerce.features.address.domain.usecases.insertupdateaddress

import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity

interface IInsertAddressUseCase {
    suspend operator fun invoke(customerAddressParams: AddressRequestEntity)
}