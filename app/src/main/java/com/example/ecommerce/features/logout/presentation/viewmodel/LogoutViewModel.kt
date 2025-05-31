package com.example.ecommerce.features.logout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.logout.domain.use_case.LogoutUseCase
import com.example.ecommerce.features.logout.presentation.event.LogoutEvent
import com.example.ecommerce.features.logout.presentation.state.LogoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _logoutEvent: Channel<UiEvent> = Channel()
    val logoutEvent = _logoutEvent.receiveAsFlow()

    private val _logoutState = MutableStateFlow(LogoutState())
    val logoutState: StateFlow<LogoutState> = _logoutState.asStateFlow()

    fun onEvent(event: LogoutEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is LogoutEvent.JwtTokenInput -> {
                    _logoutState.update { it.copy(jwtToken = evt.jwtToken) }
                }

                is LogoutEvent.FcmTokenInput -> {
                    _logoutState.update { it.copy(fcmToken = evt.fcmToken) }
                }

                is LogoutEvent.LogoutButton -> {
                    logout()
                }
            }
        }
    }

    private fun logout() = performUseCaseOperation(
        loadingUpdater = { isLoading ->
            _logoutState.update { it.copy(isLoading = isLoading) }
        },
        useCase = {
            val jwtTokenParams = _logoutState.value.jwtToken
            val fcmTokenParams = _logoutState.value.fcmToken
            if (jwtTokenParams.isEmpty() && fcmTokenParams.isEmpty()) {
                return@performUseCaseOperation
            }
            logoutUseCase(
                fcmTokenParams = fcmTokenParams,
                sessionTokenParams = jwtTokenParams
            )
            _logoutEvent.send(
                UiEvent.Navigation.SignIn(R.id.loginFragment)
            )
        },
        onFailure = { failure ->
            _logoutEvent.send(
                UiEvent.ShowSnackBar(
                    message = failure.message ?: Unknown_Error,
                )
            )
        },
        onException = {
            _logoutEvent.send(
                UiEvent.ShowSnackBar(
                    message = it.message ?: Unknown_Error,
                )
            )
        },
    )
}