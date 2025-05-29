package com.example.ecommerce.features.notification.presentation.viewmodel.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val addFcmTokenDeviceUseCase: IAddFcmTokenDeviceUseCase
) : ViewModel(), INotificationViewModel {
    private val _notificationState =
        MutableSharedFlow<UiState<Any>>(replay = 0)
    override val notificationState: SharedFlow<UiState<Any>> get() = _notificationState.asSharedFlow()

    override fun addFcmTokenDevice(token: String) {
        viewModelScope.launch {
            _notificationState.emit(UiState.Loading("addFcmTokenDevice"))
            try {
                addFcmTokenDeviceUseCase.invoke(token)
                _notificationState.emit(UiState.Success(Unit, "addFcmTokenDevice"))
            } catch (failure: Failures) {
                _notificationState.emit(UiState.Error(mapFailureMessage(failure), "addFcmTokenDevice"))
            } catch (e: Exception) {
                _notificationState.emit(UiState.Error("${e.message}", "addFcmTokenDevice"))
            }

        }
    }
}