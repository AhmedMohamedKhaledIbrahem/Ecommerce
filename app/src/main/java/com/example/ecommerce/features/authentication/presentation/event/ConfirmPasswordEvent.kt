package com.example.ecommerce.features.authentication.presentation.event

sealed class ConfirmPasswordEvent {
    data class UserIdInput(val userId: Int) : ConfirmPasswordEvent()
    data class OtpInput(val otp: Int) : ConfirmPasswordEvent()
    data class NewPasswordInput(val newPassword: String) : ConfirmPasswordEvent()
    object ConfirmPasswordButton : ConfirmPasswordEvent()

}