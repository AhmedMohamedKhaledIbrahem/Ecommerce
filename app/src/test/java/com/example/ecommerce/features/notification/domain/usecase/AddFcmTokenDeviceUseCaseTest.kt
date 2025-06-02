package com.example.ecommerce.features.notification.domain.usecase

import com.example.ecommerce.features.notification.domain.repository.NotificationRepository
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.AddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.domain.usecase.addfcmtokendevice.IAddFcmTokenDeviceUseCase
import com.example.ecommerce.features.notification.tToken
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
class AddFcmTokenDeviceUseCaseTest {
//    @Mock
//    lateinit var repository: NotificationRepository
//    private lateinit var addFcmTokenDeviceUseCase: IAddFcmTokenDeviceUseCase
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.openMocks(this)
//        addFcmTokenDeviceUseCase = AddFcmTokenDeviceUseCase(repository)
//    }
//
//    @Test
//    fun `addFcmTokenDeviceUseCase should call repository saveToken method`() = runTest {
//        `when`(repository.saveToken(token = tToken)).thenReturn(Unit)
//        addFcmTokenDeviceUseCase.invoke(token = tToken)
//        verify(repository).saveToken(token = tToken)
//        verifyNoMoreInteractions(repository)
//    }
}