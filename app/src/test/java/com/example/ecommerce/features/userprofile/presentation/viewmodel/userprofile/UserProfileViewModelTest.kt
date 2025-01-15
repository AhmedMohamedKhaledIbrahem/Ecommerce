package com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.observerViewModelErrorState
import com.example.ecommerce.features.observerViewModelSuccessState
import com.example.ecommerce.features.removeObserverFromLiveData
import com.example.ecommerce.features.userprofile.domain.usecases.getuserprofilebyid.IGetUserProfileUseCase
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
import org.mockito.kotlin.verify
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class UserProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getUserProfileUseCase: IGetUserProfileUseCase
    private lateinit var viewModel: UserProfileViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = UserProfileViewModel(getUserProfileUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    fun userProfileStateAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.userProfileState.asLiveData()
    }

    val userId = 1
    private val tGetUserProfileResponseEntity =
        UserEntity(
            userId = 1,
            userName = "test",
            userEmail = "test@gmail.com",
            firstName = "test",
            lastName = "test2",
            displayName = "test test2",
            imagePath = "",
            expiredToken = 123,
            verificationStatues = true,
            id = 1,
            roles = "customer"
        )

    @Test
    fun `getUserProfile should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getUserProfileUseCase.invoke()).thenReturn(tGetUserProfileResponseEntity)
        viewModel.getUserProfile()
        val observer = observerViewModelSuccessState(
            latch,
            tGetUserProfileResponseEntity,
            userProfileStateAsLiveData()
        )
        verify(getUserProfileUseCase).invoke()
        await(latch)
        removeObserverFromLiveData(userProfileStateAsLiveData(),observer)
    }
    @Test
    fun `getUserProfile should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        val errorMessage = "Error message"
        `when`(getUserProfileUseCase.invoke()).thenThrow(RuntimeException(errorMessage))
        viewModel.getUserProfile()
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            userProfileStateAsLiveData()
        )
        verify(getUserProfileUseCase).invoke()
        await(latch)
        removeObserverFromLiveData(userProfileStateAsLiveData(),observer)
    }
}