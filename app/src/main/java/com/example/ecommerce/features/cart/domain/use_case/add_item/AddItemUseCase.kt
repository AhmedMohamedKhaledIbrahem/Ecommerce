package com.example.ecommerce.features.cart.domain.use_case.add_item

import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.domain.repository.CartRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) : IAddItemUseCase {
    override suspend fun invoke(addItemParams: AddItemRequestEntity) {
        cartRepository.addItem(addItemParams = addItemParams)
    }

}