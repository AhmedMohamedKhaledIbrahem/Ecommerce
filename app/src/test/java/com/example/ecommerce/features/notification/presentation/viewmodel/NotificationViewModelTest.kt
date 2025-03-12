package com.example.ecommerce.features.notification.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.errors.mapFailureMessage
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.presentation.viewmodel.notification.INotificationViewModel
import com.example.ecommerce.features.notification.presentation.viewmodel.notification.NotificationViewModel
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class NotificationViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var addFcmTokenDeviceUseCase: IAddFcmTokenDeviceUseCase
    private lateinit var viewModel: INotificationViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private val latch = CountDownLatch(1)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = NotificationViewModel(addFcmTokenDeviceUseCase = addFcmTokenDeviceUseCase)

    }

    private fun notificationStateUiAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.notificationState.asLiveData()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addFcmTokenDevice should emit notificationState with success state when use case succeeds`() =
        runTest {
            `when`(addFcmTokenDeviceUseCase(token = tToken)).thenReturn(Unit)
            viewModel.addFcmTokenDevice(token = tToken)
            val observer = observerViewModelSuccessState(
                latch = latch,
                Unit,
                notificationStateUiAsLiveData()
            )
            await(latch = latch)
            verify(addFcmTokenDeviceUseCase).invoke(token = tToken)
            removeObserverFromLiveData(notificationStateUiAsLiveData(), observer)

        }

    @Test
    fun `addFcmTokenDevice should emit notificationState with error state when use case throw exception`() =
        runTest {
            `when`(addFcmTokenDeviceUseCase(token = tToken)).thenThrow(RuntimeException(errorMessage))
            viewModel.addFcmTokenDevice(token = tToken)
            val observer = observerViewModelErrorState(
                latch = latch,
                errorMessage,
                notificationStateUiAsLiveData()
            )
            await(latch = latch)
            verify(addFcmTokenDeviceUseCase).invoke(token = tToken)
            removeObserverFromLiveData(notificationStateUiAsLiveData(), observer)
        }

    @Test
    fun `addFcmTokenDevice should emit notificationState with error state when use case throw Failure type`() =
        runTest {
            val failure = Failures.ServerFailure(errorMessage)
            `when`(addFcmTokenDeviceUseCase(token = tToken)).thenAnswer { throw failure }
            viewModel.addFcmTokenDevice(token = tToken)
            val expectedErrorState = mapFailureMessage(failure)
            val observer = observerViewModelErrorState(
                latch = latch,
                expectedErrorState,
                notificationStateUiAsLiveData()
            )
            await(latch = latch)
            verify(addFcmTokenDeviceUseCase).invoke(token = tToken)
            removeObserverFromLiveData(notificationStateUiAsLiveData(), observer)
        }


}