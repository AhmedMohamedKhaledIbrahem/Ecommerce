package com.example.ecommerce.features.logout.data.repository

import com.example.ecommerce.R
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.logout.data.source.local.LogoutLocalDataSource
import com.example.ecommerce.features.logout.data.source.remote.LogoutRemoteDataSource
import com.example.ecommerce.features.logout.domain.repository.LogoutRepository
import com.example.ecommerce.features.serverFailure
import com.example.ecommerce.features.serverFailureMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LogoutRepositoryTest {
    private val localDataSource = mockk<LogoutLocalDataSource>()
    private val remoteDataSource = mockk<LogoutRemoteDataSource>()
    private val internetConnectionChecker = mockk<InternetConnectionChecker>()
    private lateinit var repository: LogoutRepository
    private val tToken = "abc12:ab"

    @Before
    fun setup() {
        repository = LogoutRepositoryImp(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }

    @Test
    fun `logout should call removeFcmToken and logout on success when internet connection is available`() =
        runTest {
            coEvery { internetConnectionChecker.hasConnection() } returns true
            coEvery { remoteDataSource.removeFcmToken(tToken) } returns Unit
            coEvery { localDataSource.logout() } returns Unit
            repository.logout(tToken)
            coVerify(exactly = 1) { internetConnectionChecker.hasConnection() }
            coVerify(exactly = 1) { remoteDataSource.removeFcmToken(tToken) }
            coVerify(exactly = 1) { localDataSource.logout() }
        }

    @Test
    fun `logout should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            coEvery { internetConnectionChecker.hasConnection() } returns false
            val connectionFailure = connectionFailure {
                repository.logout(tToken)
            }
            assertEquals(R.string.no_internet_connection, connectionFailure.resourceId)
            coVerify(exactly = 1) { internetConnectionChecker.hasConnection() }
            coVerify(exactly = 0) { remoteDataSource.removeFcmToken(tToken) }
            coVerify(exactly = 0) { localDataSource.logout() }
        }
    @Test
    fun `logout should throw ServerFailure when internet connection is  available`() =
        runTest {
            coEvery { internetConnectionChecker.hasConnection() } returns true
            coEvery { remoteDataSource.removeFcmToken(tToken) } throws Failures.ServerFailure(serverFailureMessage)

            val serverFailure = serverFailure {
                repository.logout(tToken)
            }
            assertEquals(serverFailureMessage, serverFailure.message)
            coVerify(exactly = 1) { internetConnectionChecker.hasConnection() }
            coVerify(exactly = 1) { remoteDataSource.removeFcmToken(tToken) }
            coVerify(exactly = 0) { localDataSource.logout() }
        }
}