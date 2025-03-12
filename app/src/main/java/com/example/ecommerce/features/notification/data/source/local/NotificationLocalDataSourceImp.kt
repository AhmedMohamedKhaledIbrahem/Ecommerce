package com.example.ecommerce.features.notification.data.source.local

import com.example.ecommerce.core.database.data.dao.orders.OrderTagDao
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.manager.fcm.FcmDeviceToken
import javax.inject.Inject

class NotificationLocalDataSourceImp @Inject constructor(
    private val dao: OrderTagDao,
    private val fcmDeviceToken: FcmDeviceToken,

    ) : NotificationLocalDataSource {
    override suspend fun updateOrderStatus(orderId: Int, status: String) {
        try {
            dao.updateOrderStatus(orderId = orderId, status = status)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override suspend fun addFcmTokenDevice(token: String) {
        try {
            fcmDeviceToken.addFcmTokenDevice(token = token)
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override suspend fun getFcmTokenDevice(): String? {
        return try {
            fcmDeviceToken.getFcmTokenDevice()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }

    }

    override suspend fun deleteFcmTokenDevice() {
        try {
            fcmDeviceToken.deleteFcmTokenDevice()
        } catch (e: Exception) {
            throw FailureException("${e.message}")
        }
    }


}