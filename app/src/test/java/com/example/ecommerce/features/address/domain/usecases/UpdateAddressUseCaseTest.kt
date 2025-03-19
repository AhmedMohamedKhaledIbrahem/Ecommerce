package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.updateaddress.UpdateAddressUseCase
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

//@ExperimentalCoroutinesApi
//class UpdateAddressUseCaseTest {
//    @Mock
//    private lateinit var repository: AddressRepository
//    private lateinit var updateAddressUseCase: UpdateAddressUseCase
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        updateAddressUseCase = UpdateAddressUseCase(repository = repository)
//    }
//
//
//    @Test
//    fun `invoke should call updateAddress on the repository`() = runTest {
//        `when`(repository.updateAddress(any())).thenReturn(Unit)
//        updateAddressUseCase.invoke(customerAddressParams = AddressRequestEntity())
//        verify(repository).updateAddress(AddressRequestEntity())
//        verifyNoMoreInteractions(repository)
//    }
//}