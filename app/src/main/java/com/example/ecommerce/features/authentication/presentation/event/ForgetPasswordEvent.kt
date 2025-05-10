package com.example.ecommerce.features.authentication.presentation.event

sealed class ForgetPasswordEvent {
    data class EmailInput(val value: String) : ForgetPasswordEvent()
    data object ForgetPasswordButton : ForgetPasswordEvent()

}