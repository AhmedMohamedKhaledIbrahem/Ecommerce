package com.example.ecommerce.features.notification.data.repository

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.connectionFailureMessage
import com.example.ecommerce.features.notification.data.source.remote.NotificationRemoteDataSource
import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import com.example.ecommerce.features.notification.tToken
import com.example.ecommerce.features.serverFailure
import com.example.ecommerce.features.serverFailureMessage
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class NotificationRepositoryTest {
    @Mock
    lateinit var internetConnectionChecker: InternetConnectionChecker

    @Mock
    lateinit var remoteDataSource: NotificationRemoteDataSource
    private lateinit var repository: NotificationRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = NotificationRepositoryImp(
            remoteDataSource = remoteDataSource,
            connectionChecker = internetConnectionChecker
        )
    }

    @Test
    fun `saveToken should call remoteDataSource when internet connection is available`() = runTest {
        `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.saveToken(token = tToken)).thenReturn(Unit)
        repository.saveToken(token = tToken)
        verify(remoteDataSource).saveToken(token = tToken)
    }

    @Test
    fun `save should throw ConnectionFailure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val result = connectionFailure {
                repository.saveToken(token = tToken)
            }
            assertEquals(connectionFailureMessage, result.message)
        }

    @Test
    fun `saveToken should throw ServerFailure when remoteDataSource throws exception`() = runTest {
        `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.saveToken(token = tToken)).thenThrow(
            FailureException(serverFailureMessage)
        )
        val result = serverFailure {
            repository.saveToken(token = tToken)
        }
        assertEquals(serverFailureMessage, result.message)

    }
}