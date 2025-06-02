package com.example.ecommerce.features.notification.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.notification.domain.entity.NotificationRequestEntity
import com.example.ecommerce.features.notification.domain.entity.NotificationResponseEntity
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.presentation.event.NotificationEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class NotificationViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val addFcmTokenDeviceUseCase = mockk<IAddFcmTokenDeviceUseCase>()
    private lateinit var viewModel: NotificationViewModel
    private val tToken = "ad32:a"

    private val notificationResponseEntity = mockk<NotificationResponseEntity>()

    @Before
    fun setup() {
        viewModel = NotificationViewModel(addFcmTokenDeviceUseCase)
    }

    @Test
    fun `onEvent AddFcmTokenDevice should update the Input Value`() = runTest {
        val job = activateTestFlow(viewModel.notificationState)
        viewModel.onEvent(NotificationEvent.AddFcmTokenDevice(tToken))
        val state = viewModel.notificationState.value.token
        assertEquals(tToken, state)
        job.cancel()
    }

    @Test
    fun `onEvent OnAddFcmTokenDevice button should trigger the addFcmTokenDeviceUseCase`() =
        runTest {
            val logoutSpy = spyk(viewModel, recordPrivateCalls = true)
            logoutSpy.onEvent(NotificationEvent.OnAddFcmTokenDevice)
            coVerify(exactly = 1) { logoutSpy[ADD_FCM_TOKEN_DEVICE]() }
        }

    @Test
    fun `addFcmTokenDevice should call addFcmTokenDeviceUseCase`() = runTest {
        viewModel.onEvent(NotificationEvent.AddFcmTokenDevice(tToken))
        advanceUntilIdle()

        val state = viewModel.notificationState.value.token
        val notificationRequestEntity = NotificationRequestEntity(state)
        coEvery { addFcmTokenDeviceUseCase.invoke(notificationRequestEntity) } returns notificationResponseEntity

        viewModel.onEvent(NotificationEvent.OnAddFcmTokenDevice)
        advanceUntilIdle()
        coVerify(exactly = 1) { addFcmTokenDeviceUseCase.invoke(notificationRequestEntity) }

    }

    @Test
    fun `addFcmTokenDevice throw Failure Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(NotificationEvent.AddFcmTokenDevice(tToken))
        advanceUntilIdle()

        val state = viewModel.notificationState.value.token
        val notificationRequestEntity = NotificationRequestEntity(state)
        coEvery { addFcmTokenDeviceUseCase.invoke(notificationRequestEntity) } throws Failures.ServerFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.notificationEvent.first() }
        viewModel.onEvent(NotificationEvent.OnAddFcmTokenDevice)
        advanceUntilIdle()


        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `addFcmTokenDevice throw  Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(NotificationEvent.AddFcmTokenDevice(tToken))
        advanceUntilIdle()

        val state = viewModel.notificationState.value.token
        val notificationRequestEntity = NotificationRequestEntity(state)
        coEvery { addFcmTokenDeviceUseCase.invoke(notificationRequestEntity) } throws Exception(
            errorMessage
        )

        val eventDeferred = async { viewModel.notificationEvent.first() }
        viewModel.onEvent(NotificationEvent.OnAddFcmTokenDevice)
        advanceUntilIdle()


        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    companion object {
        private const val ADD_FCM_TOKEN_DEVICE = "addFcmTokenDevice"
    }

}