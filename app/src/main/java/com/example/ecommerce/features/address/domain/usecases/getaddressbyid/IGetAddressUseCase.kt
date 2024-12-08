package com.example.ecommerce.features.address.domain.usecases.getaddressbyid

import com.example.ecommerce.core.data.entities.CustomerAddressEntity


interface IGetAddressUseCase {
    suspend operator fun invoke(id:Int): CustomerAddressEntity
}