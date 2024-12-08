package com.example.ecommerce.features.address.domain.usecases.getaddress

import com.example.ecommerce.core.data.entities.CustomerAddressEntity


interface IGetAddressUseCase {
    suspend operator fun invoke(): CustomerAddressEntity
}