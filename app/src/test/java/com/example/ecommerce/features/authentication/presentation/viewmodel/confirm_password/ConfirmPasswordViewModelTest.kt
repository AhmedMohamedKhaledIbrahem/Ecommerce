package com.example.ecommerce.features.authentication.presentation.viewmodel.confirm_password

import com.example.ecommerce.R
import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.authentication.domain.entites.ConfirmPasswordResetRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.usecases.confirm_password_reset.ConfirmPasswordChangeUseCase
import com.example.ecommerce.features.authentication.presentation.event.ConfirmPasswordEvent
import com.example.ecommerce.features.errorMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ConfirmPasswordViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val confirmPasswordUseCase = mockk<ConfirmPasswordChangeUseCase>()
    private lateinit var viewModel: ConfirmPasswordViewModel
    private val tUserId = 1
    private val tOtp = 123456
    private val tNewPassword = "123456"
    private val messageResponse = mockk<MessageResponseEntity>()

    @Before
    fun setup() {
        viewModel = ConfirmPasswordViewModel(confirmPasswordUseCase)
    }

    @Test
    fun `onEvent UserIdInput update state`() = runTest {
        val tUserId = 1
        val job = activateTestFlow(viewModel.confirmPasswordState)
        viewModel.onEvent(ConfirmPasswordEvent.UserIdInput(tUserId))
        advanceUntilIdle()
        val state = viewModel.confirmPasswordState.value.userid
        TestCase.assertEquals(tUserId, state)
        job.cancel()
    }

    @Test
    fun `onEvent NewPasswordInput update state`() = runTest {
        val tNewPassword = "123456"
        val job = activateTestFlow(viewModel.confirmPasswordState)
        viewModel.onEvent(ConfirmPasswordEvent.NewPasswordInput(tNewPassword))
        advanceUntilIdle()
        val state = viewModel.confirmPasswordState.value.newPassword
        TestCase.assertEquals(tNewPassword, state)
        job.cancel()
    }

    @Test
    fun `onEvent OtpInput update state`() = runTest {
        val tOTp = 123456
        val job = activateTestFlow(viewModel.confirmPasswordState)
        viewModel.onEvent(ConfirmPasswordEvent.OtpInput(tOTp))
        advanceUntilIdle()
        val state = viewModel.confirmPasswordState.value.otp
        TestCase.assertEquals(tOTp, state)
        job.cancel()
    }

    @Test
    fun `onEvent Button confirmPassword should trigger confirmPasswordUseCase`() = runTest {
        val confirmPasswordSpy = spyk(viewModel, recordPrivateCalls = true)
        confirmPasswordSpy.onEvent(ConfirmPasswordEvent.ConfirmPasswordButton)
        coVerify(exactly = 1) { confirmPasswordSpy[CONFIRM_PASSWORD]() }
    }

    @Test
    fun `confirmPassword should call confirmPasswordUseCase`() = runTest {
        viewModel.onEvent(ConfirmPasswordEvent.UserIdInput(tUserId))
        viewModel.onEvent(ConfirmPasswordEvent.OtpInput(tOtp))
        viewModel.onEvent(ConfirmPasswordEvent.NewPasswordInput(tNewPassword))
        advanceUntilIdle()

        val confirmPasswordRequestEntity = ConfirmPasswordResetRequestEntity(
            userId = viewModel.confirmPasswordState.value.userid,
            otp = viewModel.confirmPasswordState.value.otp,
            password = viewModel.confirmPasswordState.value.newPassword
        )
        coEvery { confirmPasswordUseCase.invoke(confirmPasswordRequestEntity) } returns messageResponse

        val eventDeferred = async { viewModel.confirmPasswordEvent.first() }
        viewModel.onEvent(ConfirmPasswordEvent.ConfirmPasswordButton)
        advanceUntilIdle()

        coVerify(exactly = 1) { confirmPasswordUseCase.invoke(confirmPasswordRequestEntity) }
        val event = eventDeferred.await()
        assertIs<UiEvent.ShowSnackBar>(event)
        assertFalse(viewModel.confirmPasswordState.value.isLoading)

    }

    @Test
    fun `confirmPassword should throw failure and send ShowSnackBar event`() = runTest {
        viewModel.onEvent(ConfirmPasswordEvent.UserIdInput(tUserId))
        viewModel.onEvent(ConfirmPasswordEvent.OtpInput(tOtp))
        viewModel.onEvent(ConfirmPasswordEvent.NewPasswordInput(tNewPassword))
        advanceUntilIdle()

        val confirmPasswordRequestEntity = ConfirmPasswordResetRequestEntity(
            userId = viewModel.confirmPasswordState.value.userid,
            otp = viewModel.confirmPasswordState.value.otp,
            password = viewModel.confirmPasswordState.value.newPassword
        )
        coEvery { confirmPasswordUseCase.invoke(confirmPasswordRequestEntity) } throws Failures.ServerFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.confirmPasswordEvent.first() }
        viewModel.onEvent(ConfirmPasswordEvent.ConfirmPasswordButton)
        advanceUntilIdle()
        val event = eventDeferred.await()

        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, event.message)
        assertFalse(viewModel.confirmPasswordState.value.isLoading)
    }

    @Test
    fun `confirmPassword should throw Exception and send ShowSnackBar event`() = runTest {
        viewModel.onEvent(ConfirmPasswordEvent.UserIdInput(tUserId))
        viewModel.onEvent(ConfirmPasswordEvent.OtpInput(tOtp))
        viewModel.onEvent(ConfirmPasswordEvent.NewPasswordInput(tNewPassword))
        advanceUntilIdle()

        val confirmPasswordRequestEntity = ConfirmPasswordResetRequestEntity(
            userId = viewModel.confirmPasswordState.value.userid,
            otp = viewModel.confirmPasswordState.value.otp,
            password = viewModel.confirmPasswordState.value.newPassword
        )
        coEvery { confirmPasswordUseCase.invoke(confirmPasswordRequestEntity) } throws Exception(
            errorMessage
        )

        val eventDeferred = async { viewModel.confirmPasswordEvent.first() }
        viewModel.onEvent(ConfirmPasswordEvent.ConfirmPasswordButton)
        advanceUntilIdle()
        val event = eventDeferred.await()

        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(R.string.change_password_error, event.resId)
        assertFalse(viewModel.confirmPasswordState.value.isLoading)
    }

    companion object {
        private const val CONFIRM_PASSWORD = "confirmPassword"
    }
}