package com.example.ecommerce.features.orders.domain.usecase

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import com.example.ecommerce.features.orders.domain.use_case.clear_orders.ClearOrderUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ClearOrderUseCaseTest {
    @Mock
    private lateinit var orderRepository: OrderRepository
    private lateinit var clearOrderUseCase: ClearOrderUseCase

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        clearOrderUseCase = ClearOrderUseCase(orderRepository)
    }

    @Test
    fun `invoke should call clearOrders from the repository`() = runTest {
        `when`(orderRepository.clearOrders()).thenReturn(Unit)
        clearOrderUseCase.invoke()
        verify(orderRepository).clearOrders()
        verifyNoMoreInteractions(orderRepository)

    }

}