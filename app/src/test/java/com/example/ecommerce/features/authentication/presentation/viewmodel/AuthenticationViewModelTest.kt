package com.example.ecommerce.features.authentication.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.ecommerce.features.authentication.data.mapper.AuthenticationMapper
import com.example.ecommerce.features.authentication.data.mapper.MessageResponseMapper
import com.example.ecommerce.features.authentication.data.models.AuthenticationResponseModel
import com.example.ecommerce.features.authentication.data.models.MessageResponseModel
import com.example.ecommerce.features.authentication.domain.entites.AuthenticationRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.EmailRequestEntity
import com.example.ecommerce.features.authentication.domain.entites.SignUpRequestEntity
import com.example.ecommerce.features.authentication.domain.usecases.login.ILoginUseCase
import com.example.ecommerce.features.authentication.domain.usecases.logout.ILogoutUseCase
import com.example.ecommerce.features.authentication.domain.usecases.reestpassword.IResetPasswordUseCase
import com.example.ecommerce.features.authentication.domain.usecases.signup.ISignUpUseCase
import com.example.ecommerce.features.authentication.presentation.viewmodel.authenticationviewmodel.AuthenticationViewModel
import com.example.ecommerce.features.authentication.presentation.viewmodel.state.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class AuthenticateViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: AuthenticationViewModel

    @Mock
    private lateinit var loginUseCase: ILoginUseCase

    @Mock
    private lateinit var signUpUseCase: ISignUpUseCase

    @Mock
    private lateinit var resetPasswordUseCase: IResetPasswordUseCase

    @Mock
    private lateinit var logoutUseCase: ILogoutUseCase
    private val mainDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainDispatcher)
        viewModel = AuthenticationViewModel(
            loginUseCase,
            signUpUseCase,
            resetPasswordUseCase,
            logoutUseCase
        )

    }

    @After
    fun resetMain() {
        Dispatchers.resetMain()
    }

    private fun observerAuthenticationViewModelSuccessState(
        latch: CountDownLatch,
        expected: Any
    ): Observer<UiState<Any>> {
        val observer = Observer<UiState<Any>> { state ->
            if (state is UiState.Success) {
                assertEquals(
                    expected,
                    state.data
                )
                latch.countDown()
            }
        }
        viewModel.authenticationState.observeForever(observer)
        return observer
    }

    private fun observerAuthenticationViewModelErrorState(
        latch: CountDownLatch,
        expectedException: String
    ): Observer<UiState<Any>> {
        val observer = Observer<UiState<Any>> { state ->
            if (state is UiState.Error) {
                assertEquals(
                    expectedException,
                    state.message
                )
                latch.countDown()
            }
        }
        viewModel.authenticationState.observeForever(observer)
        return observer
    }

    private fun removeObserverAuthenticationViewModel(observer: Observer<UiState<Any>>) {
        viewModel.authenticationState.observeForever(observer)
    }

    private val tLoginParams = AuthenticationRequestEntity(userName = "test", password = "123456")
    private val loginResponse = fixture("login.json").run {
        Gson().fromJson(this, AuthenticationResponseModel::class.java)
    }
    private val tAuthenticationResponseEntity = AuthenticationMapper.mapToEntity(loginResponse)
    private val tSignUpParams = SignUpRequestEntity(
        username = "test",
        email = "test@gmail.com",
        firstName = "jino",
        lastName = "pero",
        password = "123456",
    )
    private val messageResponse = fixture("message.json").run {
        Gson().fromJson(this, MessageResponseModel::class.java)
    }
    private val tMessageResponseEntity = MessageResponseMapper.mapToEntity(messageResponse)
    private val tResetPasswordParams = EmailRequestEntity(email = "test@gmail.com")

    @Test
    fun `Verifies that the AuthenticationViewModel handles successful login operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(loginUseCase.invoke(loginParams = any())).thenReturn(
                tAuthenticationResponseEntity
            )
            val observer =
                observerAuthenticationViewModelSuccessState(latch, tAuthenticationResponseEntity)
            viewModel.login(loginParams = tLoginParams)
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)
        }


    @Test
    fun `Verifies that the AuthenticationViewModel handles failed login operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            val expectedError = RuntimeException("Login failed")
            `when`(loginUseCase.invoke(loginParams = any())).thenThrow(expectedError)
            val observer = observerAuthenticationViewModelErrorState(latch, "Login failed")
            viewModel.login(loginParams = tLoginParams)
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)

        }

    @Test
    fun `Verifies that the AuthenticationViewModel handles successful signUp operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(signUpUseCase.invoke(signUpParams = any())).thenReturn(tMessageResponseEntity)
            val observer =
                observerAuthenticationViewModelSuccessState(latch, tMessageResponseEntity.message)
            viewModel.signUp(signUpParams = tSignUpParams)
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)
        }

    @Test
    fun `Verifies that the AuthenticationViewModel handles failed signUp operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            val expectedError = RuntimeException("signUp failed")
            `when`(loginUseCase.invoke(loginParams = any())).thenThrow(expectedError)
            val observer = observerAuthenticationViewModelErrorState(latch, "signUp failed")

            viewModel.login(loginParams = tLoginParams)
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)

        }

    @Test
    fun `Verifies that the AuthenticationViewModel handles successful resetPassword operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(resetPasswordUseCase.invoke(resetPasswordParams = any())).thenReturn(
                tMessageResponseEntity
            )
            val observer =
                observerAuthenticationViewModelSuccessState(latch, tMessageResponseEntity.message)
            viewModel.resetPassword(resetPasswordParams = tResetPasswordParams)
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)
        }

    @Test
    fun `Verifies that the AuthenticationViewModel handles failed resetPassword operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            val expectedError = RuntimeException("resetPassword failed")
            `when`(resetPasswordUseCase.invoke(resetPasswordParams = any())).thenThrow(expectedError)
            val observer = observerAuthenticationViewModelErrorState(latch, "resetPassword failed")

            viewModel.resetPassword(resetPasswordParams = tResetPasswordParams)
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)

        }

    @Test
    fun `Verifies that the AuthenticationViewModel handles successful logout operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(logoutUseCase.invoke()).thenReturn(Unit)
            val observer =
                observerAuthenticationViewModelSuccessState(latch, Unit)
            viewModel.logout()
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)
        }

    @Test
    fun `Verifies that the AuthenticationViewModel handles failed logout operations correctly`() =
        runTest {
            val latch = CountDownLatch(1)
            val expectedError = RuntimeException("logout failed")
            `when`(logoutUseCase.invoke()).thenThrow(expectedError)
            val observer = observerAuthenticationViewModelErrorState(latch, "logout failed")

            viewModel.logout()
            await(latch = latch)
            removeObserverAuthenticationViewModel(observer)

        }

}



