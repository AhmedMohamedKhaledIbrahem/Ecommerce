package com.example.ecommerce.features.address.domain.usecases.getselectaddress

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity

interface IGetSelectAddressUseCase {
    suspend operator fun invoke(customerId: Int): CustomerAddressEntity

}