package com.example.ecommerce.features.authentication.presentation.viewmodel.state

interface UiState<out T > {
    data object Ideal :UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class  Success<out T>(val data:T): UiState<T>
    data class  Error (val message:String) : UiState<Nothing>
}