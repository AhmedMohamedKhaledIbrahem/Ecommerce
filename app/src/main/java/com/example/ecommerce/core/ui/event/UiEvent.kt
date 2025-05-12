package com.example.ecommerce.core.ui.event

sealed class UiEvent() {

    sealed class Navigation : UiEvent() {
        data class SignUp(val destinationId: Int) : Navigation()
        data class SignIn(val destinationId: Int) : Navigation()
        data class Home(val destinationId: Int) : Navigation()
        data class CheckVerificationCode(val destinationId: Int, val args: String) : Navigation()
        data class ForgetPassword(val destinationId: Int) : Navigation()
        data class ProductDetails(val destinationId: Int, val args: Any?) : Navigation()
        data class Cart(val destinationId: Int) : Navigation()
        data class OrderDetails(val destinationId: Int) : Navigation()
        data class Orders(val destinationId: Int) : Navigation()
        data class Language(val destinationId: Int) : Navigation()
        data class Profile(val destinationId: Int) : Navigation()
        data class EditProfile(val destinationId: Int) : Navigation()
        data class Setting(val destinationId: Int) : Navigation()
    }

    data class ShowSnackBar(val message: String) : UiEvent()
    data class CombinedEvents(val events: List<UiEvent>) : UiEvent()
}

inline fun  navigationWithArgs(
    event: UiEvent,
    crossinline onNavigate: (destinationId: Int, args: Any?) -> Unit
) {
    when (event) {
        is UiEvent.Navigation.ProductDetails -> {
            onNavigate(event.destinationId, event.args)
        }

        else -> Unit
    }
}