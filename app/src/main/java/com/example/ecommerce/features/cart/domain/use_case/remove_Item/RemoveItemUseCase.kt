package com.example.ecommerce.features.cart.domain.use_case.remove_Item

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class RemoveItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) : IRemoveItemUseCase {
    override suspend fun invoke(keyItem: String) {
        cartRepository.removeItem(keyItem = keyItem)
    }
}