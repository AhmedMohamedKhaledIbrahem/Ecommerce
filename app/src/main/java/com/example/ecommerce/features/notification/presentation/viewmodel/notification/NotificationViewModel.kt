package com.example.ecommerce.features.notification.presentation.viewmodel.notification

import androidx.lifecycle.ViewModel
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.extension.eventHandler
import com.example.ecommerce.core.extension.performUseCaseOperation
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.presentation.event.NotificationEvent
import com.example.ecommerce.features.notification.presentation.state.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val addFcmTokenDeviceUseCase: IAddFcmTokenDeviceUseCase
) : ViewModel() {
    private val _notificationEvent: Channel<UiEvent> = Channel()
    val notificationEvent = _notificationEvent.receiveAsFlow()

    private val _notificationState = MutableStateFlow(NotificationState())
    val notificationState: StateFlow<NotificationState> = _notificationState.asStateFlow()

    fun onEvent(event: NotificationEvent) {
        eventHandler(event = event) { evt ->
            when (evt) {
                is NotificationEvent.AddFcmTokenDevice -> {
                    _notificationState.update { it.copy(token = evt.token) }
                }

                is NotificationEvent.OnAddFcmTokenDevice -> {
                    addFcmTokenDevice()
                }
            }
        }
    }

    private fun addFcmTokenDevice() = performUseCaseOperation(
        useCase = {
            val token = notificationState.value.token
            if (token.isEmpty()) {
                return@performUseCaseOperation
            }
            val notificationRequestParams = NotificationRequestEntity(token = token)
            addFcmTokenDeviceUseCase(notificationRequestParams = notificationRequestParams)
        },
        onFailure = { failure ->
            val mapFailureToMessage = mapFailureMessage(failure)
            _notificationEvent.send(UiEvent.ShowSnackBar(message = mapFailureToMessage))
        },
        onException = {
            _notificationEvent.send(UiEvent.ShowSnackBar(message = it.message.toString()))
        },
    )


}