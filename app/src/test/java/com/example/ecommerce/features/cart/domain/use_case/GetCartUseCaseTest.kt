package com.example.ecommerce.features.cart.domain.use_case

import com.example.ecommerce.core.database.data.entities.cart.CartEntity
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.features.cart.domain.repository.CartRepository
import com.example.ecommerce.features.cart.domain.use_case.get_cart.GetCartUseCase
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
class GetCartUseCaseTest {
    @Mock
    private lateinit var repository: CartRepository
    private lateinit var useCase: GetCartUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetCartUseCase(cartRepository = repository)
    }

    private val tCartEntity = CartEntity(cartId = "1")
    private val tItemEntity = listOf(
        ItemCartEntity(
            cartId = "1",
            quantity = 1,
            name = "test",
            price = "1000",
            itemId = 1,
            itemHashKey = "1te2st",
            image = "test"
        )
    )

    private val tGetCartParams = CartWithItems(
        cart = tCartEntity,
        items = tItemEntity
    )

    @Test
    fun `invoke should call getCart from the repository`() = runTest {
        `when`(repository.getCart()).thenReturn(tGetCartParams)
        useCase.invoke()
        verify(repository).getCart()
        verifyNoMoreInteractions(repository)
    }
}