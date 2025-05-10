package com.example.ecommerce.features.authentication.presentation.viewmodel

import com.example.ecommerce.R
import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.ISignUpUseCase
import com.example.ecommerce.features.authentication.presentation.event.SignUpEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.signup.SignUpViewModel
import com.example.ecommerce.features.errorMessage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase
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
class SignUpViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private lateinit var signUpViewModel: SignUpViewModel
    private val signUpUseCase = mockk<ISignUpUseCase>()
    private val sendVerificationCodeUseCase = mockk<ISendVerificationCodeUseCase>()

    @Before
    fun setup() {
        signUpViewModel = SignUpViewModel(
            signUpUseCase = signUpUseCase,
            sendVerificationCodeUseCase = sendVerificationCodeUseCase
        )

    }


    @Test
    fun `onEvent userNameInput update state`() = runTest {
        val testUserName = "test"
        val job = activateTestFlow(signUpViewModel.signUpState)
        signUpViewModel.onEvent(SignUpEvent.Input.UserName(testUserName))
        advanceUntilIdle()
        val state = signUpViewModel.signUpState.value.userName
        assertEquals(testUserName, state)
        job.cancel()
    }

    @Test
    fun `onEvent firstNameInput update state`() = runTest {
        val testFirstName = "test"
        val job = activateTestFlow(signUpViewModel.signUpState)
        signUpViewModel.onEvent(SignUpEvent.Input.FirstName(testFirstName))
        advanceUntilIdle()
        val state = signUpViewModel.signUpState.value.firstName
        assertEquals(testFirstName, state)
        job.cancel()
    }

    @Test
    fun `onEvent lastNameInput update state`() = runTest {
        val testLastName = "test"
        val job = activateTestFlow(signUpViewModel.signUpState)
        signUpViewModel.onEvent(SignUpEvent.Input.LastName(testLastName))
        advanceUntilIdle()
        val state = signUpViewModel.signUpState.value.lastName
        assertEquals(testLastName, state)
        job.cancel()
    }

    @Test
    fun `onEvent emailInput update state`() = runTest {
        val testEmail = "test"
        val job = activateTestFlow(signUpViewModel.signUpState)
        signUpViewModel.onEvent(SignUpEvent.Input.Email(testEmail))
        advanceUntilIdle()
        val state = signUpViewModel.signUpState.value.email
        assertEquals(testEmail, state)
        job.cancel()
    }

    @Test
    fun `onEvent PasswordInput update state`() = runTest {
        val testPassword = "123456a"
        val job = activateTestFlow(signUpViewModel.signUpState)
        signUpViewModel.onEvent(SignUpEvent.Input.Password(testPassword))
        advanceUntilIdle()
        val state = signUpViewModel.signUpState.value.password
        assertEquals(testPassword, state)
        job.cancel()
    }

    @Test
    fun `onEvent Button SignUp should call signUpUseCase`() = runTest {
        val signSpy = spyk(signUpViewModel, recordPrivateCalls = true)
        signSpy.onEvent(SignUpEvent.Button.SignUp)
        coVerify(exactly = 1) { signSpy["signUp"]() }
    }

    @Test
    fun `onEvent Button SignIn should send Navigation SignIn event`() = runTest {
        val emittedEvent = async {
            signUpViewModel.signUpEvent.first()
        }
        signUpViewModel.onEvent(SignUpEvent.Button.SignIn)
        val event = emittedEvent.await()
        assertTrue(event is UiEvent.Navigation.SignIn)
        assertEquals(R.id.loginFragment, event.destinationId)
        emittedEvent.cancel()
    }

    @Test
    fun `signUp success then navigate to CheckVerificationCode`() = runTest {
        signUpViewModel.onEvent(SignUpEvent.Input.UserName("test"))
        signUpViewModel.onEvent(SignUpEvent.Input.FirstName("test"))
        signUpViewModel.onEvent(SignUpEvent.Input.LastName("example"))
        signUpViewModel.onEvent(SignUpEvent.Input.Email("test@test.com"))
        signUpViewModel.onEvent(SignUpEvent.Input.Password("123456a"))
        val expectedSignUpParams = SignUpRequestEntity(
            username = signUpViewModel.signUpState.value.userName,
            firstName = signUpViewModel.signUpState.value.firstName,
            lastName = signUpViewModel.signUpState.value.lastName,
            email = signUpViewModel.signUpState.value.email,
            password = signUpViewModel.signUpState.value.password
        )
        val message = MessageResponseEntity(message = "success", verified = false)
        coEvery { signUpUseCase.invoke(expectedSignUpParams) } returns message
        val sendRequest = EmailRequestEntity(signUpViewModel.signUpState.value.email)
        coEvery { sendVerificationCodeUseCase.invoke(sendRequest) } returns message
        val events = mutableListOf<UiEvent>()
        val collector = launch { signUpViewModel.signUpEvent.collect { events.add(it) } }
        signUpViewModel.onEvent(SignUpEvent.Button.SignUp)
        advanceUntilIdle()
        assertTrue(events.isNotEmpty())
        val combinedEvent = events.first()
        assertTrue(combinedEvent is UiEvent.CombinedEvents)
        val eventInCombined = combinedEvent.events
        assertEquals(2, eventInCombined.size)
        assertTrue(eventInCombined[0] is UiEvent.ShowSnackBar)
        assertTrue(eventInCombined[1] is UiEvent.Navigation.CheckVerificationCode)
        collector.cancel()
        assertFalse(signUpViewModel.signUpState.value.isLoading)
    }
    @Test
    fun `signUp with failure then show snackBar`() = runTest {
        val tFailure = Failures.ServerFailure(errorMessage)
        signUpViewModel.onEvent(SignUpEvent.Input.UserName("test"))
        signUpViewModel.onEvent(SignUpEvent.Input.FirstName("test"))
        signUpViewModel.onEvent(SignUpEvent.Input.LastName("example"))
        signUpViewModel.onEvent(SignUpEvent.Input.Email("test@test.com"))
        signUpViewModel.onEvent(SignUpEvent.Input.Password("123456a"))
        val expectedSignUpParams = SignUpRequestEntity(
            username = signUpViewModel.signUpState.value.userName,
            firstName = signUpViewModel.signUpState.value.firstName,
            lastName = signUpViewModel.signUpState.value.lastName,
            email = signUpViewModel.signUpState.value.email,
            password = signUpViewModel.signUpState.value.password
        )
        coEvery { signUpUseCase.invoke(expectedSignUpParams) } throws tFailure
        val eventDeferred = async { signUpViewModel.signUpEvent.first() }
        signUpViewModel.onEvent(SignUpEvent.Button.SignUp)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, event.message)
        assertFalse(signUpViewModel.signUpState.value.isLoading)
    }

}