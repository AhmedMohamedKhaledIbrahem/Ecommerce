package com.example.ecommerce.features.cart.domain.use_case

import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.domain.repository.CartRepository
import com.example.ecommerce.features.cart.domain.use_case.add_item.AddItemUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class AddItemUseCaseTest {
    @Mock
    private lateinit var repository: CartRepository
    private lateinit var useCase: AddItemUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = AddItemUseCase(cartRepository = repository)
    }

    private val tAddItemParams = AddItemRequestEntity(
        id = "1",
        quantity = "1"
    )

    @Test
    fun `invoke should call addItem from the repository`() = runTest {
        `when`(repository.addItem(tAddItemParams)).thenReturn(Unit)
         useCase(tAddItemParams)
        verify(repository).addItem(addItemParams = tAddItemParams)
        verifyNoMoreInteractions(repository)

    }
}