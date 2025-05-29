package com.example.ecommerce.features.address.domain.usecases.selectaddress

interface ISelectAddressUseCase {
    suspend operator fun invoke(customerId: Int)

}