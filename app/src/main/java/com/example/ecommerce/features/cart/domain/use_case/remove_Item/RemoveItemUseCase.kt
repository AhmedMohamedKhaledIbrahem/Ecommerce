package com.example.ecommerce.features.cart.domain.use_case.remove_Item

interface RemoveItemUseCase {
    suspend operator fun invoke(keyItem:String)
}