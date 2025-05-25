package com.example.ecommerce.core.extension

import androidx.lifecycle.ViewModel

inline fun <T> ViewModel.eventHandler(event: T, crossinline onEvent: (T) -> Unit) {
    onEvent(event)
}