package com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.usecases.get_image_profile_by_id.IGetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile.IUploadImageProfileUseCase
import com.example.ecommerce.features.userprofile.presentation.event.ImageProfileEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ImageProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val getImageProfileByIdUseCase = mockk<IGetImageProfileByIdUseCase>()
    private val uploadImageProfileUseCase = mockk<IUploadImageProfileUseCase>()
    lateinit var viewModel: ImageProfileViewModel

    @Before
    fun setUp() {
        viewModel = ImageProfileViewModel(
            getImageProfileByIdUseCase = getImageProfileByIdUseCase,
            uploadImageProfileUseCase = uploadImageProfileUseCase
        )

    }

    @Test
    fun `onEvent Input UploadImage should update  File state `() =
        runTest {
            val job = activateTestFlow(viewModel.imageProfileState)
            val file = File("")
            viewModel.onEvent(ImageProfileEvent.Input.UploadImage(file))
            advanceUntilIdle()
            assertEquals(
                file,
                viewModel.imageProfileState.value.file
            )
            job.cancel()
        }

    @Test
    fun `onEvent Input GetImage should update userId state `() = runTest {
        val job = activateTestFlow(viewModel.imageProfileState)
        val userId = 1
        viewModel.onEvent(ImageProfileEvent.Input.GetImage(userId))
        advanceUntilIdle()
        assertEquals(userId, viewModel.imageProfileState.value.userId)
        job.cancel()
    }

    @Test
    fun `onEvent UploadImageProfileButton should call uploadImageProfile`() = runTest {
        val imageProfileSpy = spyk(viewModel, recordPrivateCalls = true)
        imageProfileSpy.onEvent(ImageProfileEvent.UploadImageProfileButton)
        coVerify(exactly = 1) { imageProfileSpy[UPLOAD_IMAGE_PROFILE]() }
    }

    @Test
    fun `onEvent LoadImageById should call getImageProfileById`() = runTest {
        val imageProfileSpy = spyk(viewModel, recordPrivateCalls = true)
        imageProfileSpy.onEvent(ImageProfileEvent.LoadImageById)
        coVerify(exactly = 1) { imageProfileSpy[GET_IMAGE_PROFILE_BY_ID]() }
    }

    @Test
    fun `uploadImageProfile should call uploadImageProfileUseCase and update state`() =
        runTest {
            val file = File("")
            viewModel.onEvent(ImageProfileEvent.Input.UploadImage(file))
            advanceUntilIdle()
            val imageProfileState = viewModel.imageProfileState.value.file
            val uploadImageProfile = mockk<UploadImageProfileResponseEntity>()

            coEvery { uploadImageProfileUseCase.invoke(imageProfileState) } returns uploadImageProfile

            viewModel.onEvent(ImageProfileEvent.UploadImageProfileButton)
            advanceUntilIdle()
            coVerify(exactly = 1) { uploadImageProfileUseCase.invoke(imageProfileState) }
            assertEquals(
                uploadImageProfile,
                viewModel.imageProfileState.value.uploadImageProfileResponseEntity
            )
            assertFalse(viewModel.imageProfileState.value.isUploadImageLoading)
        }

    @Test
    fun `uploadImageProfile should throw failure and send ShowSnackBar event`() = runTest {
        val file = File("")
        viewModel.onEvent(ImageProfileEvent.Input.UploadImage(file))
        advanceUntilIdle()
        val imageProfileState = viewModel.imageProfileState.value.file
        coEvery { uploadImageProfileUseCase.invoke(imageProfileState) } throws Failures.ServerFailure(
            errorMessage
        )
        val eventDeferred = async { viewModel.imageProfileEvent.first() }
        viewModel.onEvent(ImageProfileEvent.UploadImageProfileButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `uploadImageProfile should throw Exception and send ShowSnackBar event`() = runTest {
        val file = File("")
        viewModel.onEvent(ImageProfileEvent.Input.UploadImage(file))
        advanceUntilIdle()
        val imageProfileState = viewModel.imageProfileState.value.file
        coEvery { uploadImageProfileUseCase.invoke(imageProfileState) } throws Exception(
            errorMessage
        )
        val eventDeferred = async { viewModel.imageProfileEvent.first() }
        viewModel.onEvent(ImageProfileEvent.UploadImageProfileButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `getImageProfileById should call getImageProfileByIdUseCase and update state`() =
        runTest {
            val userId = 12
            viewModel.onEvent(ImageProfileEvent.Input.GetImage(userId))
            val getImageProfile = mockk< GetImageProfileResponseEntity>()
            advanceUntilIdle()
            val userIdParams = viewModel.imageProfileState.value.userId
            coEvery { getImageProfileByIdUseCase.invoke(userIdParams) } returns  getImageProfile
            viewModel.onEvent(ImageProfileEvent.LoadImageById)
            advanceUntilIdle()
            coVerify(exactly = 1) { getImageProfileByIdUseCase.invoke(userIdParams) }
            assertEquals(
                getImageProfile,
                viewModel.imageProfileState.value.getImageProfileResponseEntity
            )
            assertFalse(viewModel.imageProfileState.value.isUploadImageLoading)
        }

    @Test
    fun `getImageProfileById should throw failure and send ShowSnackBar event`() = runTest {
        val userId = 12
        viewModel.onEvent(ImageProfileEvent.Input.GetImage(userId))
        advanceUntilIdle()
        val userIdParams = viewModel.imageProfileState.value.userId
        coEvery { getImageProfileByIdUseCase.invoke(userIdParams) } throws Failures.CacheFailure(
            errorMessage
        )
        val eventDeferred = async { viewModel.imageProfileEvent.first() }
        viewModel.onEvent(ImageProfileEvent.LoadImageById)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `getImageProfileById should throw Exception and send ShowSnackBar event`() = runTest {
        val userId = 12
        viewModel.onEvent(ImageProfileEvent.Input.GetImage(userId))
        advanceUntilIdle()
        val userIdParams = viewModel.imageProfileState.value.userId
        coEvery { getImageProfileByIdUseCase.invoke(userIdParams) } throws Exception(
            errorMessage
        )
        val eventDeferred = async { viewModel.imageProfileEvent.first() }
        viewModel.onEvent(ImageProfileEvent.LoadImageById)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }


    companion object {
        private const val UPLOAD_IMAGE_PROFILE = "uploadImageProfile"
        private const val GET_IMAGE_PROFILE_BY_ID = "getImageProfileById"

    }

}