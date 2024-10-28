package com.example.ecommerce.features.authentication.presentation.viewmodel.state

sealed class AuthenticationState<out T > {
    data object Loading : AuthenticationState<Nothing>()
    data class  Success<out T>(val login:T): AuthenticationState<T>()
    data class  Error (val message:String) : AuthenticationState<Nothing>()
}