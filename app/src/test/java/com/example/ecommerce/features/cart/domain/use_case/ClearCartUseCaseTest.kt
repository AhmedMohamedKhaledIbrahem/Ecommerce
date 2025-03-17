package com.example.ecommerce.features.cart.domain.use_case

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import com.example.ecommerce.features.cart.domain.use_case.clear_cart.ClearCartUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
class ClearCartUseCaseTest {
    @Mock
    private lateinit var repository: CartRepository
    private lateinit var useCase: ClearCartUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = ClearCartUseCase(repository = repository)

    }

    @Test
    fun `clearCart should call clearCart in repository`() = runTest {
        `when`(repository.clearCart()).thenReturn(Unit)
        useCase.invoke()
        verify(repository).clearCart()
        verifyNoMoreInteractions(repository)
    }
}