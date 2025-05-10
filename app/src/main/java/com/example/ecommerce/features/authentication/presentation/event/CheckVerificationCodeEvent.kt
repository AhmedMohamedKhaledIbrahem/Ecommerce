package com.example.ecommerce.features.authentication.presentation.event

sealed class CheckVerificationCodeEvent {
    sealed class Input : CheckVerificationCodeEvent() {
        data class Digit1(val value: String) : Input()
        data class Digit2(val value: String) : Input()
        data class Digit3(val value: String) : Input()
        data class Digit4(val value: String) : Input()
        data class Digit5(val value: String) : Input()
        data class Digit6(val value: String) : Input()
        data class Email(val value: String) : Input()
    }
    data object VerifyButton : CheckVerificationCodeEvent()

}