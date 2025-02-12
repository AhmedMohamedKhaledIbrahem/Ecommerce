package com.example.ecommerce.features.orders.domain.usecase

import com.example.ecommerce.features.orders.domain.repository.OrderRepository
import com.example.ecommerce.features.orders.domain.use_case.create_order.CreateOrderUseCase
import com.example.ecommerce.features.orders.tCreateOrderRequestEntity
import com.example.ecommerce.features.orders.tCreateOrderResponseEntity
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
class CreateOrderUseCaseTest {
    @Mock
    private lateinit var repository: OrderRepository
    private lateinit var createOrderUseCase: CreateOrderUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        createOrderUseCase = CreateOrderUseCase(repository)
    }

    @Test
    fun `invoke should call createOrder from the repository`() = runTest {
        `when`(repository.createOrder(orderRequestEntity = tCreateOrderRequestEntity)).thenReturn(
            tCreateOrderResponseEntity
        )
        val result = createOrderUseCase.invoke(orderRequestEntity = tCreateOrderRequestEntity)
        assertEquals(tCreateOrderResponseEntity, result)
        verify(repository).createOrder(orderRequestEntity = tCreateOrderRequestEntity)
        verifyNoMoreInteractions(repository)
    }
}