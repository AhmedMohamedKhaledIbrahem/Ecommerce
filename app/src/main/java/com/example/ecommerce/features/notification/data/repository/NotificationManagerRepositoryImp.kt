package com.example.ecommerce.features.notification.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.features.notification.data.source.local.NotificationLocalDataSource
import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationManagerRepositoryImp @Inject constructor(
    private val localDataSource: NotificationLocalDataSource
) : NotificationManagerRepository {
    override suspend fun addFcmTokenDevice(token: String) {
        withContext(Dispatchers.IO) {
            try {
                localDataSource.addFcmTokenDevice(token = token)
            } catch (failure: FailureException) {
                throw Failures.CacheFailure(failure.message ?: "Unknown server error")
            }

        }
    }

    override suspend fun getFcmTokenDevice(): String? {
        return withContext(Dispatchers.IO) {
            try {
                localDataSource.getFcmTokenDevice()
            } catch (failure: FailureException) {
                throw Failures.CacheFailure(failure.message ?: "Unknown server error")
            }
        }
    }

    override suspend fun deleteFcmTokenDevice() {
        withContext(Dispatchers.IO) {
            try {
                localDataSource.deleteFcmTokenDevice()
            } catch (failure: FailureException) {
                throw Failures.CacheFailure("${failure.message}")
            }
        }
    }

    override suspend fun updateOrderStatus(orderId: Int, status: String) {
        try {
            localDataSource.updateOrderStatus(orderId = orderId, status = status)
        } catch (failure: FailureException) {
            throw Failures.CacheFailure("${failure.message}")
        }

    }
}