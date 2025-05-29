package com.example.ecommerce.features.authentication.presentation.event

sealed class LoginEvent {
    data class UserNameInput(val value: String) : LoginEvent()
    data class PasswordInput(val value: String) : LoginEvent()

    sealed class Button : LoginEvent() {
        data object SignIn : LoginEvent()
        data object SignUp : LoginEvent()
        data object ForgetPassword : LoginEvent()
    }



}