package com.example.ecommerce.features.orders.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.orders.domain.use_case.fetch_orders.FetchOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.get_orders.IGetOrdersUseCase
import com.example.ecommerce.features.orders.presentation.event.OrderEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class OrderViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getOrderUseCase = mockk<IGetOrdersUseCase>()
    private val fetchOrderUseCase = mockk<FetchOrderUseCase>()
    private lateinit var viewModel: OrderViewModel


    @Before
    fun setUp() {
        viewModel = OrderViewModel(
            getOrdersUseCase = getOrderUseCase,
            fetchOrderUseCase = fetchOrderUseCase
        )
    }

    @Test
    fun `onEvent onOrderItem should update array of orderItem state`() = runTest {
        val job = activateTestFlow(viewModel.orderState)
        val orderItem = mockk<OrderItemEntity>()
        val arrayItem = arrayOf(orderItem)
        viewModel.onEvent(OrderEvent.OnOrderItemCardClick(arrayItem))
        advanceUntilIdle()
        assertEquals(arrayItem, viewModel.orderState.value.orderItem)
        job.cancel()
    }

    @Test
    fun `onEvent LoadOrders should call getOrdersUseCase`() = runTest {
        val orderSpy = spyk(viewModel, recordPrivateCalls = true)
        orderSpy.onEvent(OrderEvent.LoadOrders)
        coVerify(exactly = 1) { orderSpy[GET_ORDERS]() }
    }

    @Test
    fun `onEvent OnOrderCardClick should call orderItemCard`() = runTest {
        val orderSpy = spyk(viewModel, recordPrivateCalls = true)
        orderSpy.onEvent(OrderEvent.OnOrderCardClick)
        coVerify(exactly = 1) { orderSpy[ORDER_ITEM_CARD]() }
    }

    @Test
    fun `onEvent OnOrderCardClick should emit ShowSnackBar on exception`() = runTest {

        val viewModel = spyk(viewModel, recordPrivateCalls = true)
        every { viewModel.orderState.value.orderItem } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.orderEvent.first() }

        viewModel.onEvent(OrderEvent.OnOrderCardClick)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)


    }


    @Test
    fun `getOrders should call getOrdersUseCase and update orderState`() = runTest {
        val cartWithItems = mockk<OrderWithItems>()
        val items = listOf(cartWithItems)
        coEvery { getOrderUseCase.invoke() } returns items
        viewModel.onEvent(OrderEvent.LoadOrders)
        advanceUntilIdle()
        coVerify(exactly = 1) { getOrderUseCase.invoke() }
        assertEquals(items, viewModel.orderState.value.orders)
        assertFalse(viewModel.orderState.value.isOrdersLoading)
    }

    @Test
    fun `getOrders should throw failure and send ShowSnackBar event`() = runTest {
        coEvery { getOrderUseCase.invoke() } throws Failures.CacheFailure(errorMessage)
        val eventDeferred = async { viewModel.orderEvent.first() }
        viewModel.onEvent(OrderEvent.LoadOrders)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.orderState.value.isOrdersLoading)
    }

    @Test
    fun `getOrders should throw Exception and send ShowSnackBar event`() = runTest {
        coEvery { getOrderUseCase.invoke() } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.orderEvent.first() }
        viewModel.onEvent(OrderEvent.LoadOrders)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.orderState.value.isOrdersLoading)
    }


    companion object {
        private const val GET_ORDERS = "getOrders"
        private const val ORDER_ITEM_CARD = "orderItemCard"
    }

}