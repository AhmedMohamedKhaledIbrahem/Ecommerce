package com.example.ecommerce.features.cart.domain.use_case.update_quantity

interface IUpdateQuantityUseCase {
    suspend operator fun invoke(itemId: Int, newQuantity: Int)
}