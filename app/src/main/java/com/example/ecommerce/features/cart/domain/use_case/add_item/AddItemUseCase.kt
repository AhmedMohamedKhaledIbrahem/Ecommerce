package com.example.ecommerce.features.cart.domain.use_case.add_item

import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity

interface AddItemUseCase {
    suspend operator fun invoke(addItemParams: AddItemRequestEntity)
}