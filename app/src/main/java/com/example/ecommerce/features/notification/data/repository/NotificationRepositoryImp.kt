package com.example.ecommerce.features.notification.data.repository

import com.example.ecommerce.R
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.notification.data.mapper.toEntity
import com.example.ecommerce.features.notification.data.mapper.toModel
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSource
import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.entity.NotificationResponseEntity
import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val remoteDataSource: NotificationRemoteDataSource,
    private val connectionChecker: InternetConnectionChecker,
) : NotificationRepository {
    override suspend fun saveToken(notificationRequestParams: NotificationRequestEntity): NotificationResponseEntity {
        return try {
            if (!connectionChecker.hasConnection()) {
                throw Failures.ConnectionFailure(resourceId = R.string.no_internet_connection)
            }
            val notificationRequestModel = notificationRequestParams.toModel()
            val notificationResponseModel =
                remoteDataSource.saveToken(notificationRequestParams = notificationRequestModel)
            notificationResponseModel.toEntity()

        } catch (failure: FailureException) {
            throw Failures.ServerFailure("${failure.message}")
        }
    }
}