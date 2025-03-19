package com.example.ecommerce.features.address.domain.usecases.updateaddress

import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity

interface IUpdateAddressUseCase {
    suspend operator fun invoke(id:Int,customerAddressParams: AddressRequestEntity):UpdateAddressResponseEntity
}