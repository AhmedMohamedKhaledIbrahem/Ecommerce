package com.example.ecommerce.features.notification.domain.usecase

import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.example.ecommerce.features.notification.domain.usecase.deletefcmtokendevice.DeleteFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.deletefcmtokendevice.IDeleteFcmTokenDeviceUseCase
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

class DeleteFcmTokenDeviceUseCaseTest {
    @Mock
    private lateinit var repository: NotificationManagerRepository
    private lateinit var deleteFcmTokenDeviceUseCase: IDeleteFcmTokenDeviceUseCase

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        deleteFcmTokenDeviceUseCase = DeleteFcmTokenDeviceUseCase(repository)
    }
    @Test
    fun`deleteFcmTokenDeviceUseCase should call repository deleteFcmTokenDevice method`() = runTest{
        `when`(repository.deleteFcmTokenDevice()).thenReturn(Unit)
        deleteFcmTokenDeviceUseCase.invoke()
        verify(repository).deleteFcmTokenDevice()
        verifyNoMoreInteractions(repository)
    }
}