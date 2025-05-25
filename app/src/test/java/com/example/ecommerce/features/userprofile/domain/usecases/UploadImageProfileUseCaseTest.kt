package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.entites.UploadImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.upload_image_profile.UploadImageProfileUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import java.io.File
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class UploadImageProfileUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var uploadImageProfileUseCase: UploadImageProfileUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        uploadImageProfileUseCase = UploadImageProfileUseCase(repository = repository)
    }

    @Test
    fun `invoke should UploadImageProfile from the repository`(): Unit = runTest {
        val image = File("")
        val tUploadImageProfileResponseEntity =
            UploadImageProfileResponseEntity(message = "done", "", 1)
        `when`(repository.uploadImageProfile(image = image)).thenReturn(
            tUploadImageProfileResponseEntity
        )
        val result = uploadImageProfileUseCase.invoke(image = image)
        assertEquals(tUploadImageProfileResponseEntity, result)
        verify(repository).uploadImageProfile(image = image)
        verifyNoMoreInteractions(repository)

    }

}