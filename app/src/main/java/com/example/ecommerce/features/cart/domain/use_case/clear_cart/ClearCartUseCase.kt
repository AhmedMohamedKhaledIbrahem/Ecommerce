package com.example.ecommerce.features.cart.domain.use_case.clear_cart

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val repository: CartRepository
) : IClearCartUseCase {
    override suspend fun invoke() {
        repository.clearCart()
    }

}