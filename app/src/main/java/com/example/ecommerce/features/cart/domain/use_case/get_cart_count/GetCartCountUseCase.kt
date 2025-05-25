package com.example.ecommerce.features.cart.domain.use_case.get_cart_count

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class GetCartCountUseCase @Inject constructor(
    private val cartRepository: CartRepository
) : IGetCartCountUseCase {
    override suspend fun invoke(): Int {
        return cartRepository.getCartCount()
    }
}