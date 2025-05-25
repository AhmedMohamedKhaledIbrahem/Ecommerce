package com.example.ecommerce.features.cart.domain.use_case.get_cart_count

interface IGetCartCountUseCase {
    suspend operator fun invoke(): Int
}