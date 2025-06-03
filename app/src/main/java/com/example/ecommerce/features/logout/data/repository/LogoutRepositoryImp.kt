package com.example.ecommerce.features.logout.data.repository

import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Unknown_Error
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.logout.data.source.local.LogoutLocalDataSource
import com.example.ecommerce.features.logout.data.source.remote.LogoutRemoteDataSource
import com.example.ecommerce.features.logout.domain.repository.LogoutRepository
import javax.inject.Inject

class LogoutRepositoryImp @Inject constructor(
    private val localDataSource: LogoutLocalDataSource,
    private val remoteDataSource: LogoutRemoteDataSource,
    private val internetConnectionChecker: InternetConnectionChecker
) : LogoutRepository {
    override suspend fun logout(fcmTokenParams: String) {
        try {
            if (!internetConnectionChecker.hasConnection()) throw Failures.ConnectionFailure(
                resourceId = R.string.no_internet_connection
            )
            remoteDataSource.removeFcmToken(tokenParams = fcmTokenParams)
            localDataSource.logout()
        } catch (e: FailureException) {
            throw Failures.ServerFailure(e.message ?: Unknown_Error)
        }
    }
}