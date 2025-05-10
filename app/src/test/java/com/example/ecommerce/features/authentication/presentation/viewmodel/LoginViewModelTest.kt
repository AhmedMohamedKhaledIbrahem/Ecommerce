package com.example.ecommerce.features.authentication.presentation.viewmodel

import com.example.ecommerce.R
import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.customer.CustomerManager
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationResponseEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.MessageResponseEntity
import com.example.ecommerce.features.authentication.domain.usecases.login.ILoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.sendverificationcode.ISendVerificationCodeUseCase
import com.example.ecommerce.features.authentication.presentation.event.LoginEvent
import com.example.ecommerce.features.authentication.presentation.viewmodel.login.LoginViewModel
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice.IGetFcmTokenDeviceUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var loginViewModel: LoginViewModel
    private val loginUseCase = mockk<ILoginUseCase>()
    private val sendVerificationCodeUseCase = mockk<ISendVerificationCodeUseCase>()
    private val addFcmTokenDeviceUseCase = mockk<IAddFcmTokenDeviceUseCase>()
    private val getFcmTokenDeviceUseCase = mockk<IGetFcmTokenDeviceUseCase>()
    private val customerManager = mockk<CustomerManager>()

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            sendVerificationCodeUseCase = sendVerificationCodeUseCase,
            addFcmTokenDeviceUseCase = addFcmTokenDeviceUseCase,
            getFcmTokenDeviceUseCase = getFcmTokenDeviceUseCase,
            customerManager = customerManager
        )
    }

    private fun tAuthResponse(): AuthenticationResponseEntity {
        return AuthenticationResponseEntity(
            userId = 123,
            userEmail = "test@example.com",
            userName = "test",
            firstName = "test",
            lastName = "example",
            token = "fake_token",
            displayName = "test example",
            roles = listOf("customer"),
            expiredToken = 123456,
            verificationStatues = true,
        )
    }

    private fun tExpectedLoginParams(userName: String, password: String) =
        AuthenticationRequestEntity(
            userName = userName,
            password = password
        )


    private val testUserName = "test"
    private val testPassword = "2"
    private val testUserId = 123
    private val testFcmToken = "fcmToken456"


    @Test
    fun `onEvent userNameInput update state`() = runTest {
        val job = activateTestFlow(loginViewModel.loginState)
        loginViewModel.onEvent(LoginEvent.UserNameInput(testUserName))
        advanceUntilIdle()
        val state = loginViewModel.loginState.value.userName
        assertEquals(testUserName, state)
        job.cancel()
    }

    @Test
    fun `onEvent passwordInput update state`() = runTest {
        val job = activateTestFlow(loginViewModel.loginState)
        loginViewModel.onEvent(LoginEvent.PasswordInput(testPassword))
        advanceUntilIdle()
        val state = loginViewModel.loginState.value.password
        assertEquals(testPassword, state)
        job.cancel()
    }

    @Test
    fun `onEvent Button SignIn should call loginUseCase`() = runTest {
        val loginSpy = spyk(loginViewModel, recordPrivateCalls = true)
        loginSpy.onEvent(LoginEvent.Button.SignIn)
        coVerify(exactly = 1) { loginSpy["signIn"]() }
    }

    @Test
    fun `onEvent Button SignUp should send Navigation SignUp event`() = runTest {
        val emittedEvent = async {
            loginViewModel.loginEvent.first()
        }
        loginViewModel.onEvent(LoginEvent.Button.SignUp)
        val event = emittedEvent.await()
        assertTrue(event is UiEvent.Navigation.SignUp)
        assertEquals(R.id.signUpFragment, (event as UiEvent.Navigation.SignUp).destinationId)
        emittedEvent.cancel()
    }

    @Test
    fun `onEvent Button ForgetPassword should send Navigation ForgetPassword event`() = runTest {
        val emittedEvent = async {
            loginViewModel.loginEvent.first()
        }
        loginViewModel.onEvent(LoginEvent.Button.ForgetPassword)
        val event = emittedEvent.await()
        assertTrue(event is UiEvent.Navigation.ForgetPassword)
        assertEquals(R.id.forgetPasswordFragment, (event as UiEvent.Navigation.ForgetPassword).destinationId)
        emittedEvent.cancel()
    }


    @Test
    fun `signIn success with verification true then navigate to Home Screen`() = runTest {

        loginViewModel.onEvent(LoginEvent.UserNameInput(testUserName))
        loginViewModel.onEvent(LoginEvent.PasswordInput(testPassword))

        val expectedLoginParams =
            tExpectedLoginParams(
                userName = loginViewModel.loginState.value.userName,
                password = loginViewModel.loginState.value.password
            )

        coEvery { loginUseCase.invoke(expectedLoginParams) } returns tAuthResponse()
        coEvery { customerManager.setCustomerId(testUserId) } just Runs
        coEvery { getFcmTokenDeviceUseCase.invoke() } returns testFcmToken
        coEvery { addFcmTokenDeviceUseCase.invoke(testFcmToken) } just Runs
        val eventDeferred = async { loginViewModel.loginEvent.first() }
        loginViewModel.onEvent(LoginEvent.Button.SignIn)
        advanceUntilIdle()
        coVerify(exactly = 1) { loginUseCase.invoke(expectedLoginParams) }
        coVerify(exactly = 1) { customerManager.setCustomerId(testUserId) }
        coVerify(exactly = 1) { getFcmTokenDeviceUseCase.invoke() }
        coVerify(exactly = 1) { addFcmTokenDeviceUseCase.invoke(testFcmToken) }
        val channelEvent = eventDeferred.await()
        assertIs<UiEvent.Navigation.Home>(channelEvent)
        assertEquals(R.id.productFragment, channelEvent.destinationId)
        assertFalse(loginViewModel.loginState.value.isLoading)

    }

    @Test
    fun `signIn with unverified user should send verification code and navigate to CheckVerificationCode screen`() =
        runTest {
            val messageResponseEntity = MessageResponseEntity(
                message = "Check your email",
                false
            )
            val sendVerification = EmailRequestEntity(tAuthResponse().userEmail)
            loginViewModel.onEvent(LoginEvent.UserNameInput(testUserName))
            loginViewModel.onEvent(LoginEvent.PasswordInput(testPassword))
            val expectedLoginParams =
                tExpectedLoginParams(
                    userName = loginViewModel.loginState.value.userName,
                    password = loginViewModel.loginState.value.password
                )

            coEvery { loginUseCase.invoke(expectedLoginParams) } returns tAuthResponse().copy(
                verificationStatues = false
            )
            coEvery { sendVerificationCodeUseCase.invoke(sendVerification) } returns messageResponseEntity

            val channelEvents = mutableListOf<UiEvent>()
            val collectorJob = launch {
                loginViewModel.loginEvent.collect { channelEvents.add(it) }
            }
            loginViewModel.onEvent(LoginEvent.Button.SignIn)
            advanceUntilIdle()

            coVerify(exactly = 1) { loginUseCase.invoke(expectedLoginParams) }
            coVerify(exactly = 1) { sendVerificationCodeUseCase.invoke(sendVerification) }

            assertTrue(channelEvents.isNotEmpty())
            val combinedEvent = channelEvents.first()
            assertTrue(combinedEvent is UiEvent.CombinedEvents)

            val eventsInCombined = (combinedEvent as UiEvent.CombinedEvents).events
            assertEquals(2, eventsInCombined.size)
            assertTrue(eventsInCombined.any { it is UiEvent.ShowSnackBar })
            assertTrue(eventsInCombined.any { it is UiEvent.Navigation.CheckVerificationCode })
            collectorJob.cancel()
            assertFalse(loginViewModel.loginState.value.isLoading)

        }

    @Test
    fun `signIn with Failure exception when LoginUseCase throws an exception should show snack bar with error message`() =
        runTest {
            val testFailure = Failures.ServerFailure(errorMessage)
            loginViewModel.onEvent(LoginEvent.UserNameInput(testUserName))
            loginViewModel.onEvent(LoginEvent.PasswordInput(testPassword))
            coEvery { loginUseCase.invoke(any()) } throws testFailure
            val eventDeferred = async { loginViewModel.loginEvent.first() }
            loginViewModel.onEvent(LoginEvent.Button.SignIn)
            advanceUntilIdle()
            val event = eventDeferred.await()
            assertTrue(event is UiEvent.ShowSnackBar)
            assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
            assertFalse(loginViewModel.loginState.value.isLoading)
        }
}