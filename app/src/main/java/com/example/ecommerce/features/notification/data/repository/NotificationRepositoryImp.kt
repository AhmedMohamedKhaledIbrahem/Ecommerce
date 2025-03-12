package com.example.ecommerce.features.notification.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSource
import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(
    private val remoteDataSource: NotificationRemoteDataSource,
    private val connectionChecker: InternetConnectionChecker,
) : NotificationRepository {
    override suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO){
            try {
                if (connectionChecker.hasConnection()) {
                    remoteDataSource.saveToken(token = token)
                } else {
                    throw Failures.ConnectionFailure("No Internet Connection")
                }
            } catch (failure: FailureException) {
                throw Failures.ServerFailure("${failure.message}")
            }
        }

    }
}