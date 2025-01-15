package com.example.ecommerce.features.cart.domain.use_case.get_cart

import com.example.ecommerce.core.database.data.entities.cart.CartWithItems

interface IGetCartUseCase {
    suspend operator fun invoke():CartWithItems
}