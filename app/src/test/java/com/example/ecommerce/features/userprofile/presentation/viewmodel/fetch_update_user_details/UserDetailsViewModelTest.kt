package com.example.ecommerce.features.userprofile.presentation.viewmodel.fetch_update_user_details

import com.example.ecommerce.R
import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.userprofile.domain.entites.UpdateUserDetailsRequestEntity
import com.example.ecommerce.features.userprofile.domain.usecases.fetch_update_user_details.IFetchUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.update_user_details.IUpdateUserDetailsUseCase
import com.example.ecommerce.features.userprofile.presentation.event.UserDetailsEvent
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val fetchUpdateUserDetailsUseCase = mockk<IFetchUpdateUserDetailsUseCase>()
    private val updateUserDetailsUseCase = mockk<IUpdateUserDetailsUseCase>()
    private lateinit var viewModel: UserDetailsViewModel

    @Before
    fun setup() {
        viewModel = UserDetailsViewModel(
            fetchUpdateUserDetailsUseCase = fetchUpdateUserDetailsUseCase,
            updateUserDetailsUseCase = updateUserDetailsUseCase
        )
    }

    @Test
    fun `onEvent UserDetailsUpdateInput should update updateUserDetailsRequestEntity state `() =
        runTest {
            val job = activateTestFlow(viewModel.userDetailsState)
            val updateUserDetailsRequestEntity = mockk<UpdateUserDetailsRequestEntity>()
            viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateInput(updateUserDetailsRequestEntity))
            advanceUntilIdle()
            assertEquals(
                updateUserDetailsRequestEntity,
                viewModel.userDetailsState.value.updateUserDetailsRequestEntity
            )
            job.cancel()
        }

    @Test
    fun `onEvent UserDetailsUpdateButton should call updateUserDetails`() = runTest {
        val userDetailsSpy = spyk(viewModel, recordPrivateCalls = true)
        userDetailsSpy.onEvent(UserDetailsEvent.UserDetailsUpdateButton)
        coVerify(exactly = 1) { userDetailsSpy[UPDATE_USER_DETAILS]() }
    }

    @Test
    fun `onEvent LoadUserDetailsCheck should call fetchUpdateUserDetails`() = runTest {
        val userDetailsSpy = spyk(viewModel, recordPrivateCalls = true)
        userDetailsSpy.onEvent(UserDetailsEvent.LoadUserDetailsCheck)
        coVerify(exactly = 1) { userDetailsSpy[FETCH_UPDATE_USER_DETAILS]() }
    }

    @Test
    fun `fetchUpdateUserDetails should call fetchUpdateUserDetailsUseCase and update state`() =
        runTest {
            val displayName = "ahmed"
            coEvery { fetchUpdateUserDetailsUseCase.invoke() } returns displayName
            viewModel.onEvent(UserDetailsEvent.LoadUserDetailsCheck)
            advanceUntilIdle()
            coVerify(exactly = 1) { fetchUpdateUserDetailsUseCase.invoke() }
            assertEquals(displayName, viewModel.userDetailsState.value.displayName)
        }

    @Test
    fun `fetchUpdateUserDetails should throw failure and send ShowSnackBar event`() = runTest {
        coEvery { fetchUpdateUserDetailsUseCase.invoke() } throws Failures.CacheFailure(errorMessage)
        val eventDeferred = async { viewModel.userDetailsEvent.first() }
        viewModel.onEvent(UserDetailsEvent.LoadUserDetailsCheck)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `fetchUpdateUserDetails should throw Exception and send ShowSnackBar event`() = runTest {
        coEvery { fetchUpdateUserDetailsUseCase.invoke() } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.userDetailsEvent.first() }
        viewModel.onEvent(UserDetailsEvent.LoadUserDetailsCheck)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `updateUserDetails should call updateUserDetailsUseCase when updateUserDetailsRequestEntity is not null`() =
        runTest {
            val updateUserDetailsRequestEntity = mockk<UpdateUserDetailsRequestEntity>()
            viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateInput(updateUserDetailsRequestEntity))
            advanceUntilIdle()

            val userDetailsUpdateState =
                viewModel.userDetailsState.value.updateUserDetailsRequestEntity
            coEvery { updateUserDetailsUseCase.invoke(userDetailsUpdateState!!) } returns Unit

            val eventDeferred = async { viewModel.userDetailsEvent.first() }
            viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateButton)
            advanceUntilIdle()

            coVerify(exactly = 1) { updateUserDetailsUseCase.invoke(userDetailsUpdateState!!) }
            assertEquals(
                updateUserDetailsRequestEntity,
                viewModel.userDetailsState.value.updateUserDetailsRequestEntity
            )

            val event = eventDeferred.await()
            assertEquals(
                R.string.update_user_details_success,
                (event as UiEvent.ShowSnackBar).resId
            )
            assertTrue(viewModel.userDetailsState.value.isUpdateSuccess)
            assertFalse(viewModel.userDetailsState.value.isUpdateLoading)
        }

    @Test
    fun `updateUserDetails should not call updateUserDetailsUseCase when params are null`() =
        runTest {
            viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateButton)
            coVerify(exactly = 0) { updateUserDetailsUseCase(any()) }
            assertFalse(viewModel.userDetailsState.value.isUpdateSuccess)
            assertFalse(viewModel.userDetailsState.value.isUpdateLoading)
        }

    @Test
    fun `updateUserDetails should throw failure and send ShowSnackBar event`() = runTest {
        val updateUserDetailsRequestEntity = mockk<UpdateUserDetailsRequestEntity>()
        viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateInput(updateUserDetailsRequestEntity))
        advanceUntilIdle()

        val userDetailsUpdateState =
            viewModel.userDetailsState.value.updateUserDetailsRequestEntity

        coEvery { updateUserDetailsUseCase.invoke(userDetailsUpdateState!!) } throws Failures.CacheFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.userDetailsEvent.first() }
        viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.userDetailsState.value.isUpdateLoading)
    }

    @Test
    fun `updateUserDetails should throw Exception and send ShowSnackBar event`() = runTest {
        val updateUserDetailsRequestEntity = mockk<UpdateUserDetailsRequestEntity>()
        viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateInput(updateUserDetailsRequestEntity))
        advanceUntilIdle()

        val userDetailsUpdateState =
            viewModel.userDetailsState.value.updateUserDetailsRequestEntity

        coEvery { updateUserDetailsUseCase.invoke(userDetailsUpdateState!!) } throws Failures.CacheFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.userDetailsEvent.first() }
        viewModel.onEvent(UserDetailsEvent.UserDetailsUpdateButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.userDetailsState.value.isUpdateLoading)
    }

    companion object {
        private const val FETCH_UPDATE_USER_DETAILS = "fetchUpdateUserDetails"
        private const val UPDATE_USER_DETAILS = "updateUserDetails"

    }

}