package com.example.ecommerce.core.ui.event

inline fun combinedEvents(
    events: List<UiEvent>,
    crossinline onShowSnackBar: (message: String) -> Unit,
    crossinline onNavigate: (destinationId: Int, args: String?) -> Unit
) {
    events.forEach { event ->
        when (event) {
            is UiEvent.ShowSnackBar -> {
                onShowSnackBar(event.message)
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

            else -> Unit
        }

    }
}

