package com.example.ecommerce.features.orders.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.ui.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.observerViewModelErrorState
import com.example.ecommerce.features.observerViewModelSuccessState
import com.example.ecommerce.features.orders.domain.use_case.clear_orders.IClearOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.get_orders.IGetOrdersUseCase
import com.example.ecommerce.features.orders.domain.use_case.save_order_locally.ISaveOrderLocallyUseCase
import com.example.ecommerce.features.orders.tCreateOrderRequestEntity
import com.example.ecommerce.features.orders.tOrderResponseEntity
import com.example.ecommerce.features.orders.tOrderWithItems
import com.example.ecommerce.features.removeObserverFromLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import java.util.concurrent.CountDownLatch

@ExperimentalCoroutinesApi
class OrderViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var createOrderUseCase: ICreateOrderUseCase

    @Mock
    private lateinit var getOrderUseCase: IGetOrdersUseCase

    @Mock
    private lateinit var clearOrdersUseCase: IClearOrderUseCase
    @Mock
    private lateinit var saveOrderLocallyUseCase: ISaveOrderLocallyUseCase
    private lateinit var viewModel: OrderViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = OrderViewModel(
            createOrderUseCase = createOrderUseCase,
            getOrdersUseCase = getOrderUseCase,
            clearOrderUseCase = clearOrdersUseCase,
            saveOrderLocallyUseCase = saveOrderLocallyUseCase
        )

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun orderStateUiAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.orderState.asLiveData()
    }

    @Test
    fun `createOrder should emit success state when createOrderUseCase returns success`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(createOrderUseCase(tCreateOrderRequestEntity)).thenReturn(
                tOrderResponseEntity
            )
            viewModel.createOrder(tCreateOrderRequestEntity)
            val observer = observerViewModelSuccessState(
                latch = latch,
                Unit,
                orderStateUiAsLiveData()
            )
            await(latch = latch)
            verify(createOrderUseCase).invoke(tCreateOrderRequestEntity)
            removeObserverFromLiveData(orderStateUiAsLiveData(), observer)
        }

    @Test
    fun `createOrder should emit error state when createOrderUseCase  throw exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(createOrderUseCase(tCreateOrderRequestEntity)).thenThrow(
            RuntimeException(errorMessage)
        )
        viewModel.createOrder(tCreateOrderRequestEntity)
        val observer = observerViewModelErrorState(
            latch = latch,
            errorMessage,
            orderStateUiAsLiveData()
        )
        await(latch = latch)
        verify(createOrderUseCase).invoke(tCreateOrderRequestEntity)
        removeObserverFromLiveData(orderStateUiAsLiveData(), observer)
    }

    @Test
    fun `getOrders should emit success state when getOrdersUseCase returns success`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(getOrderUseCase()).thenReturn(
                listOf(tOrderWithItems)
            )
            viewModel.getOrders()
            val observer = observerViewModelSuccessState(
                latch = latch,
                tOrderWithItems,
                orderStateUiAsLiveData()
            )
            await(latch = latch)
            verify(getOrderUseCase).invoke()
            removeObserverFromLiveData(orderStateUiAsLiveData(), observer)
        }

    @Test
    fun `getOrders should emit error state when getOrdersUseCase  throw exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getOrderUseCase()).thenThrow(
            RuntimeException(errorMessage)
        )
        viewModel.getOrders()
        val observer = observerViewModelErrorState(
            latch = latch,
            errorMessage,
            orderStateUiAsLiveData()
        )
        await(latch = latch)
        verify(getOrderUseCase).invoke()
        removeObserverFromLiveData(orderStateUiAsLiveData(), observer)
    }

    @Test
    fun `clearOrders should emit success state when clearOrdersUseCase returns success`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(clearOrdersUseCase()).thenReturn(
                Unit
            )
            viewModel.clearOrders()
            val observer = observerViewModelSuccessState(
                latch = latch,
                Unit,
                orderStateUiAsLiveData()
            )
            await(latch = latch)
            verify(clearOrdersUseCase).invoke()
            removeObserverFromLiveData(orderStateUiAsLiveData(), observer)
        }

    @Test
    fun `clearOrders should emit error state when clearOrdersUseCase  throw exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(clearOrdersUseCase()).thenThrow(
            RuntimeException(errorMessage)
        )
        viewModel.clearOrders()
        val observer = observerViewModelErrorState(
            latch = latch,
            errorMessage,
            orderStateUiAsLiveData()
        )
        await(latch = latch)
        verify(clearOrdersUseCase).invoke()
        removeObserverFromLiveData(orderStateUiAsLiveData(), observer)
    }
}