package com.example.ecommerce.features.address.domain.usecases.checkupdateaddress

import com.example.ecommerce.core.data.entities.CustomerAddressEntity

interface ICheckUpdateAddressUseCase {
    suspend operator fun invoke(): CustomerAddressEntity
}