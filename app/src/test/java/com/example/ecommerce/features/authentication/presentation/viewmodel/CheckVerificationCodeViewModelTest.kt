package com.example.ecommerce.features.authentication.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.authentication.domain.entites.CheckVerificationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.usecases.checkverificationcode.ICheckVerificationCodeUseCase
import com.example.ecommerce.features.authentication.presentation.event.CheckVerificationCodeEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.checkverificationcode.CheckVerificationCodeViewModel
import com.example.ecommerce.features.errorMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CheckVerificationCodeViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var checkVerificationCodeViewModel: CheckVerificationCodeViewModel
    private val checkVerificationCodeUseCase = mockk<ICheckVerificationCodeUseCase>()

    @Before
    fun setUp() {
        checkVerificationCodeViewModel =
            CheckVerificationCodeViewModel(checkVerificationCodeUseCase)
    }

    @Test
    fun `onEvent digit1 update state`() = runTest {
        val tDigit1 = "1"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit1(tDigit1))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.digit1
        assertEquals(tDigit1, state)
        job.cancel()
    }

    @Test
    fun `onEvent digit2 update state`() = runTest {
        val tDigit2 = "1"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit2(tDigit2))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.digit2
        assertEquals(tDigit2, state)
        job.cancel()
    }

    @Test
    fun `onEvent digit3 update state`() = runTest {
        val tDigit3 = "1"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit3(tDigit3))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.digit3
        assertEquals(tDigit3, state)
        job.cancel()
    }

    @Test
    fun `onEvent digit4 update state`() = runTest {
        val tDigit4 = "1"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit4(tDigit4))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.digit4
        assertEquals(tDigit4, state)
        job.cancel()
    }

    @Test
    fun `onEvent digit5 update state`() = runTest {
        val tDigit5 = "1"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit5(tDigit5))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.digit5
        assertEquals(tDigit5, state)
        job.cancel()
    }

    @Test
    fun `onEvent digit6 update state`() = runTest {
        val tDigit6 = "1"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit6(tDigit6))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.digit6
        assertEquals(tDigit6, state)
        job.cancel()
    }

    @Test
    fun `onEvent email update state`() = runTest {
        val tEmail = "test@test.com"
        val job = activateTestFlow(checkVerificationCodeViewModel.checkVerificationCodeState)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Email(tEmail))
        advanceUntilIdle()
        val state = checkVerificationCodeViewModel.checkVerificationCodeState.value.email
        assertEquals(tEmail, state)
        job.cancel()
    }

    @Test
    fun `onEvent VerifyButton should call checkVerificationCodeUseCase`() = runTest {
        val verifySpy = spyk(checkVerificationCodeViewModel, recordPrivateCalls = true)
        verifySpy.onEvent(CheckVerificationCodeEvent.VerifyButton)
        coVerify(exactly = 1) { verifySpy["verifyCode"]() }
    }

    @Test
    fun `verifyCode should be verification code is correct then navigate to Home screen`() =
        runTest {
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit1("1"))
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit2("2"))
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit3("3"))
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit4("4"))
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit5("5"))
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit6("6"))
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Email("test@test.com"))

            val otp = "123456"
            val tEmail = "test@test.com"
            val tCheckVerificationCodeParams = CheckVerificationRequestEntity(
                email = tEmail,
                code = otp
            )
            val messageResponse = MessageResponseEntity(message = "success", verified = true)
            coEvery {
                checkVerificationCodeUseCase(
                    checkVerificationCodeParams = tCheckVerificationCodeParams
                )
            } returns messageResponse
            val events = mutableListOf<UiEvent>()
            val collector = launch {
                checkVerificationCodeViewModel.checkVerificationCodeEvent.collect { events.add(it) }
            }
            checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.VerifyButton)
            advanceUntilIdle()
            coVerify(exactly = 1) { checkVerificationCodeUseCase.invoke(tCheckVerificationCodeParams) }
            assertTrue(events.isNotEmpty())
            val combinedEvent = events.first()
            assertTrue(combinedEvent is UiEvent.CombinedEvents)
            val eventInCombined = combinedEvent.events
            assertEquals(2, eventInCombined.size)
            assertTrue(eventInCombined[0] is UiEvent.ShowSnackBar)
            assertTrue(eventInCombined[1] is UiEvent.Navigation.Home)
            collector.cancel()
            assertFalse(checkVerificationCodeViewModel.checkVerificationCodeState.value.isLoading)

        }

    @Test
    fun `verifyCode should be verification code is incorrect then show snackBar `() = runTest {
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit1("1"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit2("2"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit3("3"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit4("4"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit5("5"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit6("6"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Email("test@test.com"))
        val otp = "123456"
        val tEmail = "test@test.com"
        val tCheckVerificationCodeParams = CheckVerificationRequestEntity(
            email = tEmail,
            code = otp
        )
        val messageResponse = MessageResponseEntity(message = "error message", verified = false)
        coEvery {
            checkVerificationCodeUseCase(
                checkVerificationCodeParams = tCheckVerificationCodeParams
            )
        } returns messageResponse
        val eventDeferred =
            async { checkVerificationCodeViewModel.checkVerificationCodeEvent.first() }
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.VerifyButton)
        advanceUntilIdle()
        coVerify(exactly = 1) { checkVerificationCodeUseCase.invoke(tCheckVerificationCodeParams) }
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, event.message)
        assertFalse(checkVerificationCodeViewModel.checkVerificationCodeState.value.isLoading)
    }

    @Test
    fun `verifyCode with failure then show snackBar`() = runTest {
        val tFailure = Failures.ServerFailure(errorMessage)
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit1("1"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit2("2"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit3("3"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit4("4"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit5("5"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Digit6("6"))
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.Input.Email("test@test.com"))
        coEvery {
            checkVerificationCodeUseCase(
                checkVerificationCodeParams = any()
            )
        } throws tFailure
        val eventDeferred =
            async { checkVerificationCodeViewModel.checkVerificationCodeEvent.first() }
        checkVerificationCodeViewModel.onEvent(CheckVerificationCodeEvent.VerifyButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, event.message)
        assertFalse(checkVerificationCodeViewModel.checkVerificationCodeState.value.isLoading)
    }


}