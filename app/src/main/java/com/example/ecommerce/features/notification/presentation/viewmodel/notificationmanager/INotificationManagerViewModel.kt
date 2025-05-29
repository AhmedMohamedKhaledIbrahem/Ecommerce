package com.example.ecommerce.features.notification.presentation.viewmodel.notificationmanager

import com.example.ecommerce.core.ui.state.UiState
import kotlinx.coroutines.flow.SharedFlow

interface INotificationManagerViewModel {
    val notificationManagerState: SharedFlow<UiState<Any>>
    fun getFcmTokenDevice()
    fun deleteFcmTokenDevice()
    fun <T> notificationManagerUiState(
        operation: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        source: String
    )
}