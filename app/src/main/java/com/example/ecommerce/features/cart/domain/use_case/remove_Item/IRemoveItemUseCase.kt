package com.example.ecommerce.features.cart.domain.use_case.remove_Item

interface IRemoveItemUseCase {
    suspend operator fun invoke(keyItem:String)
}