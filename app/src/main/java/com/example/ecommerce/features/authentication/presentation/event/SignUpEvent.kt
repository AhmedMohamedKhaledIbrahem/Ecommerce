package com.example.ecommerce.features.authentication.presentation.event

sealed class SignUpEvent {

    sealed class Input: SignUpEvent(){
        data class UserName(val value: String): Input()
        data class FirstName(val value: String): Input()
        data class LastName(val value: String): Input()
        data class Email(val value: String): Input()
        data class Password(val value: String): Input()
    }
    sealed class Button: SignUpEvent(){
        data object SignUp: Button()
        data object SignIn: Button()
    }

}