package com.example.ecommerce.features.category.presentation.event

sealed class CategoryEvent {
    object LoadCategory : CategoryEvent()
    object FetchCategory : CategoryEvent()

}