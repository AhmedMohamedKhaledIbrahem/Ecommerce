package com.example.ecommerce.core.service

import com.example.ecommerce.core.constants.Page
import com.example.ecommerce.core.constants.PerPage
import com.example.ecommerce.core.manager.prdouct.ProductHandler
import com.example.ecommerce.core.notification.INotification
import com.example.ecommerce.core.notification.showNotification
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.example.ecommerce.features.product.domain.repository.ProductRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


class FCMHandler : FirebaseMessagingService(), IFCMHandler {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var notificationManagerRepository: NotificationManagerRepository

    @Inject
    lateinit var notification: INotification

    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var productHandler: ProductHandler

    override fun onCreate() {
        super.onCreate()
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            FCMHandlerEntryPoint::class.java
        )
        notificationManagerRepository = entryPoint.getNotificationManagerRepository()
        notification = entryPoint.getNotification()
        productRepository = entryPoint.getProductRepository()
        productHandler = entryPoint.getProductHandler()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch {
            notificationManagerRepository.addFcmTokenDevice(token = token)
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        val source = message.data["src"]
        when (source) {
            "SRC_B" -> {
                processInternalNotification(message)

            }

            "SRC_C" -> {
                processExternalNotification(message)
            }

            else -> {
                return
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun processInternalNotification(message: RemoteMessage) {
        val title = message.notification?.title
        val body = message.notification?.body
        val status = message.data["status"] ?: "Unknown status"
        val orderId = message.data["orderId"]?.toIntOrNull() ?: -1
        if (title != null && body != null) {
            notification.showNotification(title, body, this)
            scope.launch {
                notificationManagerRepository.updateOrderStatus(
                    orderId = orderId,
                    status = status
                )
            }

        }
    }

    override fun processExternalNotification(message: RemoteMessage) {
        val isNewProduct = message.data["isNewProduct"]?.let {
            it.lowercase().toBooleanStrictOrNull() ?: false
        } ?:false
        val title = message.notification?.title
        val body = message.notification?.body
        if (title == null || body == null) return
        scope.launch {
            productHandler.addProductUpdate(update = isNewProduct)
            notification.showNotification(
                title = title,
                body = body,
                context = this@FCMHandler
            )
            productRepository.fetchProduct(page = Page, perPage = PerPage)
        }


    }

}