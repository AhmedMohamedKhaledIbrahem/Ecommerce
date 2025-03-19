package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.getaddress.GetAddressUseCase
import com.example.ecommerce.features.address.tListCustomerAddressEntity
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
class GetAddressUseCaseTest {
    @Mock
    private lateinit var repository: AddressRepository
    private lateinit var getAddressUseCase: GetAddressUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getAddressUseCase = GetAddressUseCase(repository = repository)
    }


    @Test
    fun`invoke should return list CustomerAddressEntity from the repository`() = runTest {
        `when`(repository.getAddress()).thenReturn(tListCustomerAddressEntity)
        val result = getAddressUseCase.invoke()
        assertEquals(tListCustomerAddressEntity, result)
        verify(repository).getAddress()
        verifyNoMoreInteractions(repository)
    }
}