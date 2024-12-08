package com.example.ecommerce.features.address.domain.usecases.updateaddress

import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity

interface IUpdateAddressUseCase {
    suspend operator fun invoke(customerAddressParams: AddressRequestEntity)
}