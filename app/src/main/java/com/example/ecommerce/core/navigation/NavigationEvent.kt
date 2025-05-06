package com.example.ecommerce.core.navigation

sealed class NavigationEvent {
    data class NavigateTo(val destinationId: Int) : NavigationEvent()
    data class ShowSnackBar(val message: String) : NavigationEvent()

}