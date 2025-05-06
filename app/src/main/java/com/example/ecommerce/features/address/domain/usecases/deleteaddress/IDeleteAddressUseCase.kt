package com.example.ecommerce.features.address.domain.usecases.deleteaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

interface IDeleteAddressUseCase {
    suspend operator fun invoke(customerAddressEntity: CustomerAddressEntity)

}