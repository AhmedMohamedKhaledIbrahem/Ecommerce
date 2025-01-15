package com.example.ecommerce.features.cart.domain.use_case

import com.example.ecommerce.features.cart.domain.repository.CartRepository
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.RemoveItemUseCase
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
class RemoveItemUseCaseTest {
    @Mock
    private lateinit var repository: CartRepository
    private lateinit var useCase: RemoveItemUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = RemoveItemUseCase(cartRepository = repository)

    }

    private val tKeyItem = "1"

    @Test
    fun `should invoke removeItem from the repository`() = runTest {
        `when`(repository.removeItem(tKeyItem)).thenReturn(Unit)
        useCase(tKeyItem)
        verify(repository).removeItem(keyItem = tKeyItem)
        verifyNoMoreInteractions(repository)
    }
}