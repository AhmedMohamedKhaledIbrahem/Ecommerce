package com.example.ecommerce.core.ui

sealed class UiEffect {
    data class NavigateTo(val destinationId: Int) : UiEffect()
    data object ButtonClick : UiEffect()
    data class ShowSnackBar(val message: String) : UiEffect()
}