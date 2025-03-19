package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.updateaddress.UpdateAddressUseCase
import com.example.ecommerce.features.address.id
import com.example.ecommerce.features.address.tAddressRequestEntity
import com.example.ecommerce.features.address.tUpdateAddressResponseEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class UpdateAddressUseCaseTest {
    @Mock
    private lateinit var repository: AddressRepository
    private lateinit var updateAddressUseCase: UpdateAddressUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        updateAddressUseCase = UpdateAddressUseCase(repository = repository)
    }


    @Test
    fun `invoke should call updateAddress on the repository`() = runTest {
        `when`(repository.updateAddress(id = id, customerAddressParams = tAddressRequestEntity)).thenReturn(tUpdateAddressResponseEntity)
        val result = updateAddressUseCase.invoke(customerAddressParams = tAddressRequestEntity, id = id)
        assertEquals(tUpdateAddressResponseEntity, result)
        verify(repository).updateAddress(id = id, customerAddressParams = tAddressRequestEntity)
        verifyNoMoreInteractions(repository)
    }
}