package com.example.ecommerce.features.authentication.presentation.event

sealed class ChangePasswordEvent {
    data class PasswordInput(val password: String) :
        ChangePasswordEvent()

    data class UserIdInput(val userId: Int) : ChangePasswordEvent()

    data object ChangePasswordButton : ChangePasswordEvent()

}