package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.deletealladdress.DeleteAllAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.deletealladdress.IDeleteAllAddressUseCase
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
class DeleteAllAddressUseCaseTest {
    @Mock
    private lateinit var repository: AddressRepository
    private lateinit var deleteAddressUseCase: IDeleteAllAddressUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        deleteAddressUseCase = DeleteAllAddressUseCase(repository)
    }

    @Test
    fun `invoke should call on repository deleteAddress`() = runTest {
        `when`(repository.deleteAllAddress()).thenReturn(Unit)
        deleteAddressUseCase.invoke()
        verify(repository).deleteAllAddress()
        verifyNoMoreInteractions(repository)
    }
}