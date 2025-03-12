package com.example.ecommerce.features.orders.domain.usecase

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import com.example.ecommerce.features.orders.domain.use_case.get_orders.GetOrdersUseCase
import com.example.ecommerce.features.orders.tOrderWithItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetOrdersUseCaseTest {
    @Mock
    private lateinit var repository: OrderRepository
    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getOrdersUseCase = GetOrdersUseCase(repository)
    }

    @Test
    fun `invoke should call getOrders from the repository`() = runTest {
        val tOrderWithItems = listOf(tOrderWithItems)
        `when`(repository.getOrders()).thenReturn(tOrderWithItems)
        val result = getOrdersUseCase.invoke()
        assertEquals(tOrderWithItems, result)
        verify(repository).getOrders()
        verifyNoMoreInteractions(repository)
    }
}