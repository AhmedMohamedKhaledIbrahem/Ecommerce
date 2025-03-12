package com.example.ecommerce.core.service.fcm

import android.util.Log
import com.example.ecommerce.core.notification.INotification
import com.example.ecommerce.core.notification.showNotification
import com.example.ecommerce.core.servicelocator.ServiceLocator
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrderStatusFCM : FirebaseMessagingService() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var notificationManagerRepository: NotificationManagerRepository

    @Inject
    lateinit var notification: INotification


    override fun onCreate() {
        super.onCreate()
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            OrderStatusFcmEntryPoint::class.java
        )
        notificationManagerRepository = entryPoint.getNotificationManagerRepository()
        notification = entryPoint.getNotification()

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("onNewToken", "onNewToken:is ?? $token")
        scope.launch {
            Log.e("onNewToken", "onNewToken: is ? $token")
            notificationManagerRepository.addFcmTokenDevice(token = token)

        }
    }


    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.notification?.title
        val body = message.notification?.body
        val status = message.data["status"] ?: "Unknown status"
        val orderId = message.data["orderId"] ?: "No order ID"
        if (title != null && body != null) {
            notification.showNotification(title, body, this)
            scope.launch {
                notificationManagerRepository.updateOrderStatus(
                    orderId = orderId.toInt(),
                    status = status
                )
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}