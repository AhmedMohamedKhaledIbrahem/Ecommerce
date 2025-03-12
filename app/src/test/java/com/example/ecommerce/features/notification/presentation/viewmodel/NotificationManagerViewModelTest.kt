package com.example.ecommerce.features.notification.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.notification.domain.usecase.deletefcmtokendevice.IDeleteFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice.IGetFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.presentation.viewmodel.notificationmanager.INotificationManagerViewModel
import com.example.ecommerce.features.notification.presentation.viewmodel.notificationmanager.NotificationManagerViewModel
import com.example.ecommerce.features.notification.tToken
import com.example.ecommerce.features.observerViewModelErrorState
import com.example.ecommerce.features.observerViewModelSuccessState
import com.example.ecommerce.features.removeObserverFromLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class NotificationManagerViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var deleteFcmTokenDeviceUseCase: IDeleteFcmTokenDeviceUseCase

    @Mock
    private lateinit var getFcmTokenDeviceUseCase: IGetFcmTokenDeviceUseCase
    private val dispatcher = UnconfinedTestDispatcher()
    private val latch = CountDownLatch(1)
    private lateinit var notificationManagerViewModel: INotificationManagerViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        notificationManagerViewModel = NotificationManagerViewModel(
            deleteFcmTokenDeviceUseCase = deleteFcmTokenDeviceUseCase,
            getFcmTokenDeviceUseCase = getFcmTokenDeviceUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun notificationManagerUiState(): LiveData<UiState<Any>> {
        return notificationManagerViewModel.notificationManagerState.asLiveData()
    }

    @Test
    fun `getFcmTokenDevice should emit notificationManagerState with success state when use case succeeds`() =
        runTest {
            `when`(getFcmTokenDeviceUseCase.invoke()).thenReturn(tToken)
            notificationManagerViewModel.getFcmTokenDevice()
            val observer = observerViewModelSuccessState(
                latch = latch,
                tToken,
                notificationManagerUiState()
            )
            await(latch = latch)
            verify(getFcmTokenDeviceUseCase).invoke()
            removeObserverFromLiveData(notificationManagerUiState(), observer)
        }

    @Test
    fun `getFcmTokenDevice should emit notificationManagerState with error state when use case throw exception`() =
        runTest {
            `when`(getFcmTokenDeviceUseCase.invoke()).thenThrow(RuntimeException(errorMessage))
            notificationManagerViewModel.getFcmTokenDevice()
            val observer = observerViewModelErrorState(
                latch = latch,
                errorMessage,
                notificationManagerUiState()
            )
            await(latch = latch)
            verify(getFcmTokenDeviceUseCase).invoke()
            removeObserverFromLiveData(notificationManagerUiState(), observer)
        }

    @Test
    fun `getFcmTokenDevice should emit notificationManagerState with error state when use case throw Failure type`() =
        runTest {
            val failure = Failures.CacheFailure(errorMessage)
            `when`(getFcmTokenDeviceUseCase.invoke()).thenAnswer { throw failure }
            notificationManagerViewModel.getFcmTokenDevice()
            val expectedErrorState = mapFailureMessage(failure)
            val observer = observerViewModelErrorState(
                latch = latch,
                expectedErrorState,
                notificationManagerUiState()
            )
            await(latch = latch)
            verify(getFcmTokenDeviceUseCase).invoke()
            removeObserverFromLiveData(notificationManagerUiState(), observer)
        }


    @Test
    fun `deleteFcmTokenDevice should emit notificationManagerState with success state when use case succeeds`() =
        runTest {
            `when`(deleteFcmTokenDeviceUseCase.invoke()).thenReturn(Unit)
            notificationManagerViewModel.deleteFcmTokenDevice()
            val observer = observerViewModelSuccessState(
                latch = latch,
                tToken,
                notificationManagerUiState()
            )
            await(latch = latch)
            verify(deleteFcmTokenDeviceUseCase).invoke()
            removeObserverFromLiveData(notificationManagerUiState(), observer)
        }

    @Test
    fun `deleteFcmTokenDevice should emit notificationManagerState with error state when use case throw exception`() =
        runTest {
            `when`(deleteFcmTokenDeviceUseCase.invoke()).thenThrow(RuntimeException(errorMessage))
            notificationManagerViewModel.deleteFcmTokenDevice()
            val observer = observerViewModelErrorState(
                latch = latch,
                errorMessage,
                notificationManagerUiState()
            )
            await(latch = latch)
            verify(deleteFcmTokenDeviceUseCase).invoke()
            removeObserverFromLiveData(notificationManagerUiState(), observer)
        }

    @Test
    fun `deleteFcmTokenDevice should emit notificationManagerState with error state when use case throw Failure type`() =
        runTest {
            val failure = Failures.CacheFailure(errorMessage)
            `when`(deleteFcmTokenDeviceUseCase.invoke()).thenAnswer { throw failure }
            notificationManagerViewModel.deleteFcmTokenDevice()
            val expectedErrorState = mapFailureMessage(failure)
            val observer = observerViewModelErrorState(
                latch = latch,
                expectedErrorState,
                notificationManagerUiState()
            )
            await(latch = latch)
            verify(deleteFcmTokenDeviceUseCase).invoke()
            removeObserverFromLiveData(notificationManagerUiState(), observer)
        }
}