package com.example.ecommerce.features.authentication.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.usecases.restpassword.IResetPasswordUseCase
import com.example.ecommerce.features.authentication.presentation.event.ForgetPasswordEvent
import com.example.ecommerce.features.authentication.presentation.event.SignUpEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.forgetpassowrd.ForgetPasswordViewModel
import com.example.ecommerce.features.errorMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class ForgetPasswordViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var forgetPasswordViewModel: ForgetPasswordViewModel
    private val resetPasswordUseCase = mockk<IResetPasswordUseCase>()

    @Before
    fun setup() {
        forgetPasswordViewModel = ForgetPasswordViewModel(resetPasswordUseCase)
    }

    private val testEmail = "test@test.com"

    @Test
    fun `onEvent EmailInput update state`() = runTest {
        val job = activateTestFlow(forgetPasswordViewModel.forgetPasswordState)

        forgetPasswordViewModel.onEvent(ForgetPasswordEvent.EmailInput(testEmail))
        advanceUntilIdle()
        val state = forgetPasswordViewModel.forgetPasswordState.value.email
        assertEquals(testEmail, state)
        job.cancel()
    }

    @Test
    fun `onEvent ForgetPasswordButton should call resetPasswordUseCase`() = runTest {
        val forgetPasswordSpy = spyk(forgetPasswordViewModel, recordPrivateCalls = true)
        forgetPasswordSpy.onEvent(ForgetPasswordEvent.ForgetPasswordButton)
        coVerify(exactly = 1) { forgetPasswordSpy["resetPassword"]() }
    }

    @Test
    fun `forgetPassword should send the link to email then navigate to login`() = runTest {
        forgetPasswordViewModel.onEvent(ForgetPasswordEvent.EmailInput(testEmail))
        val tResetPassword =
            EmailRequestEntity(email = forgetPasswordViewModel.forgetPasswordState.value.email)
        val messageResponse = MessageResponseEntity(message = "success", verified = true)
        coEvery { resetPasswordUseCase.invoke(tResetPassword) } returns messageResponse
        val events = mutableListOf<UiEvent>()
        val collector = launch {
            forgetPasswordViewModel.forgetPasswordEvent.collect { events.add(it) }
        }
        forgetPasswordViewModel.onEvent(ForgetPasswordEvent.ForgetPasswordButton)
        advanceUntilIdle()
        coVerify(exactly = 1) { resetPasswordUseCase.invoke(tResetPassword) }
        assertTrue(events.isNotEmpty())
        val combinedEvent = events.first()
        assertTrue(combinedEvent is UiEvent.CombinedEvents)
        val eventInCombined = combinedEvent.events
        assertEquals(2, eventInCombined.size)
        assertTrue(eventInCombined[0] is UiEvent.ShowSnackBar)
        assertTrue(eventInCombined[1] is UiEvent.Navigation.SignIn)
        collector.cancel()
        assertFalse(forgetPasswordViewModel.forgetPasswordState.value.isLoading)

    }
    @Test
    fun `forgetPassword with failure then show snackBar`() = runTest {
        val tFailures = Failures.ServerFailure(errorMessage)
        coEvery { resetPasswordUseCase.invoke(any()) } throws tFailures
        val eventDeferred = async { forgetPasswordViewModel.forgetPasswordEvent.first() }
        forgetPasswordViewModel.onEvent(ForgetPasswordEvent.ForgetPasswordButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        kotlin.test.assertEquals(errorMessage, event.message)
        assertFalse(forgetPasswordViewModel.forgetPasswordState.value.isLoading)
    }
}