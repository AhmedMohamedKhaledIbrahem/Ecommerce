package com.example.ecommerce.features.cart.domain.use_case.get_cart

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCaseImp @Inject constructor(
    private val cartRepository: CartRepository

) : GetCartUseCase {
    override suspend fun invoke(): CartWithItems {
        return cartRepository.getCart()
    }

}