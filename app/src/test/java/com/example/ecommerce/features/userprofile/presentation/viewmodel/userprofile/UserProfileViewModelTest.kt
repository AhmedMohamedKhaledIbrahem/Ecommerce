package com.example.ecommerce.features.userprofile.presentation.viewmodel.userprofile

import com.example.ecommerce.core.database.data.entities.user.UserEntity
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.userprofile.domain.usecases.get_user_profile.IGetUserProfileUseCase
import com.example.ecommerce.features.userprofile.presentation.event.UserProfileEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class UserProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val getUserProfileUseCase = mockk<IGetUserProfileUseCase>()
    lateinit var viewModel: UserProfileViewModel

    @Before
    fun setup() {
        viewModel = UserProfileViewModel(getUserProfileUseCase)
    }

    @Test
    fun `onEvent LoadImageById should call getImageProfileById`() = runTest {
        val userProfileSpy = spyk(viewModel, recordPrivateCalls = true)
        userProfileSpy.onEvent(UserProfileEvent.UserProfileLoad)
        coVerify(exactly = 1) { userProfileSpy[GET_USER_PROFILE]() }
    }

    @Test
    fun `getUserProfile should call getUserProfileUseCase and update state`() =
        runTest {
            val userProfile = mockk<UserEntity>()
            coEvery { getUserProfileUseCase.invoke() } returns userProfile
            viewModel.onEvent(UserProfileEvent.UserProfileLoad)
            advanceUntilIdle()
            coVerify(exactly = 1) { getUserProfileUseCase.invoke() }
            assertEquals(
                userProfile,
                viewModel.userProfileState.value.userEntity
            )
        }

    @Test
    fun `getUserProfile should throw failure and send ShowSnackBar event`() = runTest {
        coEvery {  getUserProfileUseCase.invoke()  } throws Failures.CacheFailure(
            errorMessage
        )
        val eventDeferred = async { viewModel.userProfileEvent.first() }
        viewModel.onEvent(UserProfileEvent.UserProfileLoad)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `getUserProfile should throw Exception and send ShowSnackBar event`() = runTest {
        coEvery { getUserProfileUseCase.invoke() } throws Exception(
            errorMessage
        )
        val eventDeferred = async { viewModel.userProfileEvent.first() }
        viewModel.onEvent(UserProfileEvent.UserProfileLoad)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    companion object {
        private const val GET_USER_PROFILE = "getUserProfile"
    }
}