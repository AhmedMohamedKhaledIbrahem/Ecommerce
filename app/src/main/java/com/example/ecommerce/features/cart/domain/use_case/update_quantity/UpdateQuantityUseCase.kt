package com.example.ecommerce.features.cart.domain.use_case.update_quantity

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) : IUpdateQuantityUseCase {
    override suspend fun invoke(itemId: Int, newQuantity: Int) {
        cartRepository.updateQuantity(itemId = itemId, newQuantity = newQuantity)
    }
}