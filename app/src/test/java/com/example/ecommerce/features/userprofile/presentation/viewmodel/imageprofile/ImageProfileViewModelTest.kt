package com.example.ecommerce.features.userprofile.presentation.viewmodel.imageprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.observerViewModelErrorState
import com.example.ecommerce.features.observerViewModelSuccessState
import com.example.ecommerce.features.removeObserverFromLiveData
import com.example.ecommerce.features.userprofile.data.mapper.GetImageProfileMapper
import com.example.ecommerce.features.userprofile.data.mapper.UploadImageProfileMapper
import com.example.ecommerce.features.userprofile.data.models.GetImageProfileResponseModel
import com.example.ecommerce.features.userprofile.data.models.UploadImageProfileResponseModel
import com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid.IGetImageProfileByIdUseCase
import com.example.ecommerce.features.userprofile.domain.usecases.uploadimageprofile.IUploadImageProfileUseCase
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
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import java.io.File
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class ImageProfileViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getImageProfileByIdUseCase: IGetImageProfileByIdUseCase

    @Mock
    private lateinit var uploadImageProfileUseCase: IUploadImageProfileUseCase

    private lateinit var viewModel: ImageProfileViewModel
    private val dispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = ImageProfileViewModel(
            getImageProfileByIdUseCase,
            uploadImageProfileUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

    private fun imageProfileStateAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.imageProfileState.asLiveData()
    }

    private val userId = 1
    private val tGetImageProfileResponseModel = fixture("getImageProfile.json").run {
        Gson().fromJson(this, GetImageProfileResponseModel::class.java)
    }
    private val tGetImageProfileResponseEntity = GetImageProfileMapper.mapToEntity(
        model = tGetImageProfileResponseModel
    )
    private val tUploadImageProfileResponseModel = fixture("uploadImageProfile.json").run {
        Gson().fromJson(this, UploadImageProfileResponseModel::class.java)
    }
    private val tTempFile: File = File.createTempFile("test_image", ".jpg").apply {
        writeText("dummy content")
    }
    private val tUploadImageProfileResponseEntity =
        UploadImageProfileMapper.mapToEntity(tUploadImageProfileResponseModel)
    private val terrorMessage = "error message"

    @Test
    fun `getImageProfileById should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getImageProfileByIdUseCase.invoke(userId = userId)).thenReturn(
            tGetImageProfileResponseEntity
        )
        viewModel.getImageProfileById(userId)
        val observer = observerViewModelSuccessState(
            latch,
            tGetImageProfileResponseEntity,
            imageProfileStateAsLiveData()
        )
        verify(getImageProfileByIdUseCase).invoke(userId)
        await(latch = latch)
        removeObserverFromLiveData(imageProfileStateAsLiveData(), observer)
    }

    @Test
    fun `getImageProfileById should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        val expectedException = RuntimeException(terrorMessage)
        `when`(getImageProfileByIdUseCase.invoke(userId = userId)).thenThrow(expectedException)
        viewModel.getImageProfileById(userId)
        val observer = observerViewModelErrorState(
            latch,
            terrorMessage,
            imageProfileStateAsLiveData()
        )
        verify(getImageProfileByIdUseCase).invoke(userId)
        await(latch = latch)
        removeObserverFromLiveData(imageProfileStateAsLiveData(), observer)

    }

    @Test
    fun `uploadImageProfile should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        val observe = imageProfileStateAsLiveData()
        `when`(uploadImageProfileUseCase.invoke(image = any())).thenReturn(
            tUploadImageProfileResponseEntity
        )
        viewModel.uploadImageProfile(image = tTempFile)
        val observer = observerViewModelSuccessState(
            latch,
            tUploadImageProfileResponseEntity.message,
            observe
        )
        verify(uploadImageProfileUseCase).invoke(tTempFile)
        await(latch = latch)
        removeObserverFromLiveData(observe, observer)

    }
    @Test
    fun `uploadImageProfile should emit error state when use case throws exception`()= runTest {
        val latch = CountDownLatch(1)
        val expectedException = RuntimeException(terrorMessage)
        `when`(uploadImageProfileUseCase.invoke(image = any())).thenThrow(expectedException)
        viewModel.uploadImageProfile(image = tTempFile)
        val observer = observerViewModelErrorState(
            latch,
            terrorMessage,
            imageProfileStateAsLiveData()
        )
        verify(uploadImageProfileUseCase).invoke(tTempFile)
        await(latch = latch)
        removeObserverFromLiveData(imageProfileStateAsLiveData(), observer)


    }
}