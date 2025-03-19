package com.example.ecommerce.features.address.domain.usecases.getaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity


interface IGetAddressUseCase {
    suspend operator fun invoke(): List<CustomerAddressEntity>
}