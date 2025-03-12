package com.example.ecommerce.features.notification.domain.usecase

import com.example.ecommerce.features.notification.domain.repository.NotificationManagerRepository
import com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice.GetFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.getfcmtokendevice.IGetFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.tToken
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetFcmTokenDeviceUseCaseTest {
    @Mock
    private lateinit var repository: NotificationManagerRepository
    private lateinit var getFcmTokenDeviceUseCase: IGetFcmTokenDeviceUseCase
    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        getFcmTokenDeviceUseCase = GetFcmTokenDeviceUseCase(repository)
    }

    @Test
    fun `getFcmTokenDeviceUseCase should call repository getFcmTokenDevice method`() = runTest{
        `when`(repository.getFcmTokenDevice()).thenReturn(tToken)
        getFcmTokenDeviceUseCase.invoke()
        verify(repository).getFcmTokenDevice()
        verifyNoMoreInteractions(repository)

    }
}