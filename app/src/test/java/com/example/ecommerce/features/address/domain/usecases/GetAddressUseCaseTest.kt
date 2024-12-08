package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.core.data.entities.CustomerAddressEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.getaddressbyid.GetAddressUseCase
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
class GetAddressUseCaseTest {
    @Mock
    private lateinit var repository: AddressRepository
    private lateinit var getAddressUseCase: GetAddressUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getAddressUseCase = GetAddressUseCase(repository = repository)
    }
    private val tCustomerAddressEntity = CustomerAddressEntity(
        id = 0,
        userId = 1 ,
        firstName = "John" ,
        lastName = "Doe" ,
        email = "john.c.calhoun@examplepetstore.com" ,
        phone = "1234567890" ,
        address = "123 Main St" ,
        country = "USA" ,
        city = "New York" ,
        state = "NY" ,
        zipCode = "10001" ,
    )
   private val tId = 1
    @Test
    fun`getAddressById should return CustomerAddressEntity from the repository`() = runTest {
        `when`(repository.getAddressById(id = tId)).thenReturn(tCustomerAddressEntity)
        val result = getAddressUseCase.invoke(id = tId)
        assertEquals(tCustomerAddressEntity,result)
        verify(repository).getAddressById(id = tId)
        verifyNoMoreInteractions(repository)
    }
}