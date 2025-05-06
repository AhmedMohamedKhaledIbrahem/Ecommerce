package com.example.ecommerce.features.notification.presentation.viewmodel.notification

import com.example.ecommerce.core.ui.UiState
import kotlinx.coroutines.flow.SharedFlow

interface INotificationViewModel {
    val notificationState: SharedFlow<UiState<Any>>
    fun addFcmTokenDevice(token:String)
}