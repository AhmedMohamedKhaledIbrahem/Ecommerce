package com.example.ecommerce.features.notification.presentation.viewmodel.notificationmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.features.notification.domain.usecase.deletefcmtokendevice.IDeleteFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice.IGetFcmTokenDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationManagerViewModel @Inject constructor(
    private val getFcmTokenDeviceUseCase: IGetFcmTokenDeviceUseCase,
    private val deleteFcmTokenDeviceUseCase: IDeleteFcmTokenDeviceUseCase,
    ) : ViewModel(), INotificationManagerViewModel {
    private val _notificationManagerState = MutableSharedFlow<UiState<Any>>(replay = 0)
    override val notificationManagerState: SharedFlow<UiState<Any>> get() = _notificationManagerState.asSharedFlow()

    override fun getFcmTokenDevice() {
        notificationManagerUiState(
            operation = { getFcmTokenDeviceUseCase.invoke() },
            onSuccess = { result ->
                if (result == null) return@notificationManagerUiState
                _notificationManagerState.emit(UiState.Success(result, "getFcmTokenDevice"))
            },
            source = "getFcmTokenDevice"
        )
    }

    override fun deleteFcmTokenDevice() {
        notificationManagerUiState(
            operation = { deleteFcmTokenDeviceUseCase.invoke() },
            onSuccess = { result ->
                _notificationManagerState.emit(UiState.Success(result, "deleteFcmTokenDevice"))
            },
            source = "deleteFcmTokenDevice"
        )
    }

    override fun <T> notificationManagerUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    ) {
        viewModelScope.launch {
            try {
                _notificationManagerState.emit(UiState.Loading(source))
                val result = operation()
                onSuccess(result)
            } catch (failure: Failures) {
                _notificationManagerState.emit(
                    UiState.Error(
                        mapFailureMessage(failure),
                        source
                    )
                )
            } catch (e: Exception) {
                _notificationManagerState.emit(
                    UiState.Error(
                        e.message ?: "Unknown Error",
                        source
                    )
                )
            }
        }
    }

}