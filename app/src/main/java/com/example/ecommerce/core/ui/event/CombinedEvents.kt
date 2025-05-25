package com.example.ecommerce.core.ui.event

inline fun combinedEvents(
    events: List<UiEvent>,
    crossinline onShowSnackBar: (message: String, resId: Int) -> Unit,
    crossinline onNavigate: (destinationId: Int, args: String?) -> Unit
) {
    events.forEach { event ->
        when (event) {
            is UiEvent.ShowSnackBar -> {
                onShowSnackBar(event.message, event.resId)
            }

            is UiEvent.Navigation.CheckVerificationCode -> {
                onNavigate(event.destinationId, event.args)
            }

            is UiEvent.Navigation.Home -> {
                onNavigate(event.destinationId, null)
            }

            is UiEvent.Navigation.SignIn -> {
                onNavigate(event.destinationId, null)
            }

            is UiEvent.Navigation.Cart -> {
                onNavigate(event.destinationId, null)
            }

            is UiEvent.Navigation.Orders -> {
                onNavigate(event.destinationId, null)
            }

            is UiEvent.Navigation.Address -> {
                onNavigate(event.destinationId, null)

            }

            else -> Unit
        }

    }
}

