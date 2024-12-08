package com.example.ecommerce.features.userprofile.domain.usecases

import com.example.ecommerce.features.userprofile.domain.entites.GetImageProfileResponseEntity
import com.example.ecommerce.features.userprofile.domain.repositories.UserProfileRepository
import com.example.ecommerce.features.userprofile.domain.usecases.getimageprofilebyid.GetImageProfileByIdUseCase
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlin.test.assertEquals
@ExperimentalCoroutinesApi
class GetImageProfileByIdUseCaseTest {
    @Mock
    private lateinit var repository: UserProfileRepository
    private lateinit var getImageProfileByIdUseCase: GetImageProfileByIdUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getImageProfileByIdUseCase = GetImageProfileByIdUseCase(repository = repository)
    }

    @Test
    fun `invoke should return ImageProfile from the repository`(): Unit = runTest {
        val tGetImageProfileResponseEntity =
            GetImageProfileResponseEntity(profileImage = "", userId = 1)
        val tUserId = 1
        `when`(repository.getImageProfileById(userId = tUserId)).thenReturn(
            tGetImageProfileResponseEntity
        )
        val result = getImageProfileByIdUseCase.invoke(userId = tUserId)
        assertEquals(tGetImageProfileResponseEntity,result)
        verify(repository).getImageProfileById(userId = tUserId)
        verifyNoMoreInteractions(repository)
    }
}