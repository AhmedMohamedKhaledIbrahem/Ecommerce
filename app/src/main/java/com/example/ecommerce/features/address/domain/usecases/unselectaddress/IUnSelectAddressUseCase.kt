package com.example.ecommerce.features.address.domain.usecases.unselectaddress

interface IUnSelectAddressUseCase {
    suspend operator fun invoke(customerId: Int)
}