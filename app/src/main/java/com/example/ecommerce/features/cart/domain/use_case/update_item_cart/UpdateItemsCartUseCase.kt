package com.example.ecommerce.features.cart.domain.use_case.update_item_cart

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class UpdateItemsCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
): IUpdateItemsCartUseCase {

    override suspend fun invoke() {
        cartRepository.updateItemsCart()
    }

}