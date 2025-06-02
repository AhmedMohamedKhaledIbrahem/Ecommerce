package com.example.ecommerce.features.logout.presentation.viewmodel

import com.example.ecommerce.R
import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.logout.domain.use_case.LogoutUseCase
import com.example.ecommerce.features.logout.presentation.event.LogoutEvent
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class LogoutViewModelTest {
    @get:Rule
    val mainDispatcher = MainDispatcherRule()
    private val logoutUseCase = mockk<LogoutUseCase>()
    private lateinit var viewModel: LogoutViewModel
    private val tToken = "abc:23a"

    @Before
    fun setUp() {
        viewModel = LogoutViewModel(logoutUseCase)
    }

    @Test
    fun `onEvent FcmTokenInput should update the Input Value`() = runTest {
        val job = activateTestFlow(viewModel.logoutState)
        viewModel.onEvent(LogoutEvent.FcmTokenInput(tToken))
        val state = viewModel.logoutState.value.fcmToken
        assertEquals(tToken, state)
        job.cancel()
    }

    @Test
    fun `onEvent LogoutButton button should trigger the logoutUseCase`() = runTest {
        val logoutSpy = spyk(viewModel, recordPrivateCalls = true)
        logoutSpy.onEvent(LogoutEvent.LogoutButton)
        coVerify(exactly = 1) { logoutSpy[LOGOUT]() }
    }

    @Test
    fun `logout should call logoutUseCase`() = runTest {
        viewModel.onEvent(LogoutEvent.FcmTokenInput(tToken))
        advanceUntilIdle()
        val state = viewModel.logoutState.value.fcmToken
        coEvery { logoutUseCase.invoke(state) } just Runs

        val eventDeferred = async { viewModel.logoutEvent.first() }
        viewModel.onEvent(LogoutEvent.LogoutButton)
        advanceUntilIdle()

        coVerify(exactly = 1) { logoutUseCase.invoke(tToken) }
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.Navigation.SignIn)
        assertEquals(R.id.loginFragment, (event as UiEvent.Navigation.SignIn).destinationId)
        assertFalse(viewModel.logoutState.value.isLoading)
    }

    @Test
    fun `logout throw Failure Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(LogoutEvent.FcmTokenInput(tToken))
        advanceUntilIdle()
        val state = viewModel.logoutState.value.fcmToken
        coEvery { logoutUseCase.invoke(state) } throws Failures.ServerFailure(errorMessage)

        val eventDeferred = async { viewModel.logoutEvent.first() }
        viewModel.onEvent(LogoutEvent.LogoutButton)
        advanceUntilIdle()


        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.logoutState.value.isLoading)
    }

    @Test
    fun `logout throw  Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(LogoutEvent.FcmTokenInput(tToken))
        advanceUntilIdle()
        val state = viewModel.logoutState.value.fcmToken
        coEvery { logoutUseCase.invoke(state) } throws Exception(errorMessage)

        val eventDeferred = async { viewModel.logoutEvent.first() }
        viewModel.onEvent(LogoutEvent.LogoutButton)
        advanceUntilIdle()


        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.logoutState.value.isLoading)
    }


    companion object {
        private const val LOGOUT = "logout"
    }
}