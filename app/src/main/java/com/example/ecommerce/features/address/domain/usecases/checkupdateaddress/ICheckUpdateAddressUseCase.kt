package com.example.ecommerce.features.address.domain.usecases.checkupdateaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

interface ICheckUpdateAddressUseCase {
    suspend operator fun invoke(): CustomerAddressEntity
}