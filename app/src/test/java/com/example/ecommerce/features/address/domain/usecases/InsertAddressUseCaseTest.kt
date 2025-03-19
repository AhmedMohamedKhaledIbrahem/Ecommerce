package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.InsertAddressUseCase
import com.example.ecommerce.features.address.tAddressRequestEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
class InsertAddressUseCaseTest {
    @Mock
    private lateinit var repository: AddressRepository
    private lateinit var insertAddressUseCase: InsertAddressUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        insertAddressUseCase = InsertAddressUseCase(repository = repository)
    }

    @Test
    fun `invoke should call insertAddress on the repository`() = runTest {
        `when`(repository.insertAddress(tAddressRequestEntity)).thenReturn(Unit)
        insertAddressUseCase.invoke(tAddressRequestEntity)
        verify(repository).insertAddress(tAddressRequestEntity)
        verifyNoMoreInteractions(repository)
    }

}