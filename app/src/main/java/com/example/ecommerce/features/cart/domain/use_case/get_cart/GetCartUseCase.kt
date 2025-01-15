package com.example.ecommerce.features.cart.domain.use_case.get_cart

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository

) : IGetCartUseCase {
    override suspend fun invoke(): CartWithItems {
        return cartRepository.getCart()
    }

}