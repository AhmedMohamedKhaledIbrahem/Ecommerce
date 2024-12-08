package com.example.ecommerce.core.state

interface UiState<out T > {
    data object Ideal : UiState<Nothing>
    data class Loading(val source: String) : UiState<Nothing>
    data class  Success<out T>(val data:T , val source:String): UiState<T>
    data class  Error (val message:String , val source:String) : UiState<Nothing>
}