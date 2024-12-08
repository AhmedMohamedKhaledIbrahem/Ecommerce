package com.example.ecommerce.features.address.domain.usecases

import com.example.ecommerce.features.address.domain.repositories.AddressRepository
import com.example.ecommerce.features.address.domain.usecases.checkupdateaddress.CheckUpdateAddressUseCase
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
class CheckUpdateAddressUseCaseTest {
    @Mock
    private lateinit var repository: AddressRepository
    private lateinit var checkUpdateAddressUseCase: CheckUpdateAddressUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        checkUpdateAddressUseCase = CheckUpdateAddressUseCase(repository = repository)
    }

    @Test
    fun `invoke should call checkUpdateAddress on the repository`() = runTest {
        `when`(repository.checkUpdateAddress()).thenReturn(any())
        checkUpdateAddressUseCase.invoke()
        verify(repository).checkUpdateAddress()
        verifyNoMoreInteractions(repository)
    }

}