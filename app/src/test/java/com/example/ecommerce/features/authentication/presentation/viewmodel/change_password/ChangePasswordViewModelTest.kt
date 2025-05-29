package com.example.ecommerce.features.authentication.presentation.viewmodel.change_password

import com.example.ecommerce.R
import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.authentication.domain.entites.ChangePasswordRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.usecases.change_password.ChangePasswordUseCase
import com.example.ecommerce.features.authentication.presentation.event.ChangePasswordEvent
import com.example.ecommerce.features.errorMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
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
class ChangePasswordViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val changePasswordUseCase = mockk<ChangePasswordUseCase>()
    private lateinit var viewModel: ChangePasswordViewModel

    private val messageResponse = mockk<MessageResponseEntity>()
    private val tPassword = "123456"
    private val tId = 1

    @Before
    fun setup() {
        viewModel = ChangePasswordViewModel(changePasswordUseCase)
    }

    @Test
    fun `onEvent PasswordInput update state`() = runTest {
        val tPassword = "123456"
        val job = activateTestFlow(viewModel.changePasswordState)
        viewModel.onEvent(ChangePasswordEvent.PasswordInput(tPassword))
        advanceUntilIdle()
        val state = viewModel.changePasswordState.value.password
        assertEquals(tPassword, state)
        job.cancel()
    }

    @Test
    fun `onEvent UserIdInput update state`() = runTest {
        val tId = 1
        val job = activateTestFlow(viewModel.changePasswordState)
        viewModel.onEvent(ChangePasswordEvent.UserIdInput(tId))
        advanceUntilIdle()
        val state = viewModel.changePasswordState.value.userId
        assertEquals(tId, state)
        job.cancel()
    }

    @Test
    fun `onEvent Button changePassword should call changePasswordUseCase`() = runTest {
        val changePasswordSpy = spyk(viewModel, recordPrivateCalls = true)
        changePasswordSpy.onEvent(ChangePasswordEvent.ChangePasswordButton)
        coVerify(exactly = 1) { changePasswordSpy[CHANGE_PASSWORD]() }
    }

    @Test
    fun `changePassword should call changePasswordUseCase`() = runTest {

        viewModel.onEvent(ChangePasswordEvent.PasswordInput(tPassword))
        viewModel.onEvent(ChangePasswordEvent.UserIdInput(tId))
        advanceUntilIdle()

        val password = viewModel.changePasswordState.value.password
        val userId = viewModel.changePasswordState.value.userId
        val changePasswordRequestEntity = ChangePasswordRequestEntity(
            userId = userId,
            password = password
        )
        coEvery { changePasswordUseCase.invoke(changePasswordRequestEntity) } returns messageResponse

        val eventDeferred = async { viewModel.changePasswordEvent.first() }
        viewModel.onEvent(ChangePasswordEvent.ChangePasswordButton)
        advanceUntilIdle()

        coVerify(exactly = 1) { changePasswordUseCase.invoke(changePasswordRequestEntity) }

        val event = eventDeferred.await()
        assertIs<UiEvent.ShowSnackBar>(event)
        assertTrue(viewModel.changePasswordState.value.isFinished)
        assertFalse(viewModel.changePasswordState.value.isLoading)
    }

    @Test
    fun `changePassword should throw failure and send ShowSnackBar event`() = runTest {
        viewModel.onEvent(ChangePasswordEvent.PasswordInput(tPassword))
        viewModel.onEvent(ChangePasswordEvent.UserIdInput(tId))
        advanceUntilIdle()

        val password = viewModel.changePasswordState.value.password
        val userId = viewModel.changePasswordState.value.userId
        val changePasswordRequestEntity = ChangePasswordRequestEntity(
            userId = userId,
            password = password
        )
        coEvery { changePasswordUseCase.invoke(changePasswordRequestEntity) } throws Failures.ServerFailure(
            errorMessage
        )
        val eventDeferred = async { viewModel.changePasswordEvent.first() }
        viewModel.onEvent(ChangePasswordEvent.ChangePasswordButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, event.message)
        assertFalse(viewModel.changePasswordState.value.isLoading)
    }

    @Test
    fun `changePassword should throw Exception and send ShowSnackBar event`() = runTest {
        viewModel.onEvent(ChangePasswordEvent.PasswordInput(tPassword))
        viewModel.onEvent(ChangePasswordEvent.UserIdInput(tId))
        advanceUntilIdle()

        val password = viewModel.changePasswordState.value.password
        val userId = viewModel.changePasswordState.value.userId
        val changePasswordRequestEntity = ChangePasswordRequestEntity(
            userId = userId,
            password = password
        )
        coEvery { changePasswordUseCase.invoke(changePasswordRequestEntity) } throws Exception(
            errorMessage
        )
        val eventDeferred = async { viewModel.changePasswordEvent.first() }
        viewModel.onEvent(ChangePasswordEvent.ChangePasswordButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(R.string.change_password_error, event.resId)
        assertFalse(viewModel.changePasswordState.value.isLoading)
    }


    companion object {
        private const val CHANGE_PASSWORD = "changePassword"
    }
}