package com.example.ecommerce.features.userprofile.presentation.viewmodel.usernamedetailsprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.observerViewModelErrorState
import com.example.ecommerce.features.observerViewModelSuccessState
import com.example.ecommerce.features.removeObserverFromLiveData
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserNameDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.usecases.getusernamedetails.IGetUserNameDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.updateusernamedetails.IUpdateUserNameDetailsUseCase
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class UserNameDetailsProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getUserNameDetailsUseCase: IGetUserNameDetailsUseCase

    @Mock
    private lateinit var updateUserNameDetailsUseCase: IUpdateUserNameDetailsUseCase

    private lateinit var viewModel: UserNameDetailsProfileViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = UserNameDetailsProfileViewModel(
            getUserNameDetailsUseCase,
            updateUserNameDetailsUseCase
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

    private fun userNameDetailsProfileStateAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.userNameDetailsProfileState.asLiveData()
    }

    @Test
    fun `getUserNameDetails should emit success state when use case returns data`() = runTest {

        val latch = CountDownLatch(1)
       `when`(getUserNameDetailsUseCase.invoke()).thenReturn(Unit)
        val observer =
            observerViewModelSuccessState(latch, Unit, userNameDetailsProfileStateAsLiveData())
        viewModel.getUserNameDetails()
        verify(getUserNameDetailsUseCase).invoke()
        await(latch = latch)
        removeObserverFromLiveData(userNameDetailsProfileStateAsLiveData(), observer)

    }

    @Test
    fun `getUserNameDetails should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        val errorMessage = "Test error message"
        val expectedException = RuntimeException(errorMessage)
        `when`(getUserNameDetailsUseCase.invoke()).thenThrow(expectedException)
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            userNameDetailsProfileStateAsLiveData()
        )
        viewModel.getUserNameDetails()
        verify(getUserNameDetailsUseCase).invoke()
        await(latch = latch)
        removeObserverFromLiveData(userNameDetailsProfileStateAsLiveData(), observer)
    }

    @Test
    fun `updateUserNameDetails should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        val updateUserNameDetailsParams = UpdateUserNameDetailsRequestEntity(
            id = 1,
            firstName = "test",
            lastName = "test",
            displayName = "test test"
        )
        `when`(updateUserNameDetailsUseCase.invoke(updateUserNameDetailsParams)).thenReturn(Unit)
        val observer =
            observerViewModelSuccessState(latch, Unit, userNameDetailsProfileStateAsLiveData())
        viewModel.updateUserNameDetails(updateUserNameDetailsParams)
        verify(updateUserNameDetailsUseCase).invoke(updateUserNameDetailsParams)
        await(latch = latch)
        removeObserverFromLiveData(userNameDetailsProfileStateAsLiveData(), observer)
    }

    @Test
    fun `updateUserNameDetails should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        val errorMessage = "Test error message"
        val expectedException = RuntimeException(errorMessage)
        val updateUserNameDetailsParams = UpdateUserNameDetailsRequestEntity(
            id = 1,
            firstName = "test",
            lastName = "test",
            displayName = "test test"
        )
        `when`(
            updateUserNameDetailsUseCase.invoke(any())
        ).thenThrow(
            expectedException
        )
        val observer = observerViewModelErrorState(
            latch,
            "Test error message",
            userNameDetailsProfileStateAsLiveData()
        )
        viewModel.updateUserNameDetails(updateUserNameDetailsParams)
        verify(updateUserNameDetailsUseCase).invoke(updateUserNameDetailsParams)
        await(latch = latch)
        removeObserverFromLiveData(userNameDetailsProfileStateAsLiveData(), observer)

    }


}