package com.example.ecommerce.features.cart.domain.use_case.add_item

import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity

interface IAddItemUseCase {
    suspend operator fun invoke(addItemParams: AddItemRequestEntity)
}