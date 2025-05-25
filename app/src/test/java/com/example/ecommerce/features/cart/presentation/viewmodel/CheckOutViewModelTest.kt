package com.example.ecommerce.features.cart.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.constants.CustomerOrAddressNotFound
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.address.data.mapper.AddressMapper
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.usecases.getselectaddress.IGetSelectAddressUseCase
import com.example.ecommerce.features.cart.data.mapper.ItemMapper
import com.example.ecommerce.features.cart.domain.use_case.clear_cart.IClearCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.presentation.event.CheckOutEvent
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.features.orders.domain.use_case.create_order.ICreateOrderUseCase
import com.example.ecommerce.features.orders.domain.use_case.save_order_locally.ISaveOrderLocallyUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CheckOutViewModelTest {
    @get:Rule
    val coroutineRule = MainDispatcherRule()
    private val getSelectAddressUseCase = mockk<IGetSelectAddressUseCase>()
    private val getCartUseCase = mockk<IGetCartUseCase>()
    private val createOrderUseCase = mockk<ICreateOrderUseCase>()
    private val clearCartUseCase = mockk<IClearCartUseCase>()
    private val saveOrderLocallyUseCase = mockk<ISaveOrderLocallyUseCase>()
    private lateinit var viewModel: CheckOutViewModel

    @Before
    fun setup() {
        mockkObject(AddressMapper)
        mockkObject(ItemMapper)
        viewModel = CheckOutViewModel(
            getSelectAddressUseCase = getSelectAddressUseCase,
            getCartUseCase = getCartUseCase,
            createOrderUseCase = createOrderUseCase,
            clearCartUseCase = clearCartUseCase,
            saveOrderLocallyUseCase = saveOrderLocallyUseCase
        )
    }

    @Test
    fun `onEvent Input AddressId should update addressId state`() = runTest {
        val addressId = 1
        val job = activateTestFlow(viewModel.checkOutState)
        viewModel.onEvent(CheckOutEvent.Input.AddressId(addressId = addressId))
        advanceUntilIdle()
        val state = viewModel.checkOutState.value.addressId
        assertEquals(addressId, state)
        job.cancel()
    }

    @Test
    fun `onEvent Input CustomerId should update customerId state`() = runTest {
        val customerId = 1
        val job = activateTestFlow(viewModel.checkOutState)
        viewModel.onEvent(CheckOutEvent.Input.CustomerId(customerId = customerId))
        advanceUntilIdle()
        val state = viewModel.checkOutState.value.customerId
        assertEquals(customerId, state)
        job.cancel()
    }

    @Test
    fun `checkout should create order when the customerId and addressId are valid`() = runTest {
        val job = activateTestFlow(viewModel.checkOutState)

        val customerId = 1
        val addressId = 1
        val customerAddressEntity = mockk<CustomerAddressEntity>()
        val cartWithItems = mockk<CartWithItems>()
        val orderResponse = mockk<OrderResponseEntity>()
        val mockBillingInfo = mockk<BillingInfoRequestEntity>(relaxed = true)
        val mockLineItem = listOf(mockk<LineItemRequestEntity>(relaxed = true))

        viewModel.onEvent(CheckOutEvent.Input.CustomerId(customerId))
        viewModel.onEvent(CheckOutEvent.Input.AddressId(addressId))
        advanceUntilIdle()

        coEvery { getSelectAddressUseCase(addressId) } returns customerAddressEntity
        coEvery { getCartUseCase() } returns cartWithItems

        every { AddressMapper.mapCustomerAddressEntityToBillingInfoRequest(customerAddressEntity) } returns mockBillingInfo
        every { ItemMapper.mapCartWithItemsToLineItemRequest(cartWithItems) } returns mockLineItem

        coEvery { createOrderUseCase(any()) } returns orderResponse
        coEvery { clearCartUseCase() } just Runs

        val events = mutableListOf<UiEvent>()
        val collectJob = launch {
            viewModel.checkOutEvent.collect {
                events.add(it)
            }
        }
        viewModel.onEvent(CheckOutEvent.CheckoutButton)
        advanceUntilIdle()

        coVerify(exactly = 1) { getSelectAddressUseCase(viewModel.checkOutState.value.customerId) }
        coVerify(exactly = 1) { getCartUseCase() }
        coVerify(exactly = 1) { createOrderUseCase(any()) }
        coVerify(exactly = 1) { clearCartUseCase() }


        assertTrue(events.isNotEmpty())
        val combinedEvent = events.first()
        assertTrue(combinedEvent is UiEvent.CombinedEvents)
        val eventsInCombined = combinedEvent.events
        assertEquals(2, eventsInCombined.size)
        assertTrue(eventsInCombined.any { it is UiEvent.ShowSnackBar })
        assertTrue(eventsInCombined.any { it is UiEvent.Navigation.Orders })
        collectJob.cancel()
        assertFalse(viewModel.checkOutState.value.isCheckingOut)
        job.cancel()

    }

    @Test
    fun `checkout should not create order when the customerId is invalid and addressId is invalid`() =
        runTest {
            val job = activateTestFlow(viewModel.checkOutState)
            val eventDeferred = async { viewModel.checkOutEvent.first() }

            viewModel.onEvent(CheckOutEvent.Input.CustomerId(-1))
            viewModel.onEvent(CheckOutEvent.Input.AddressId(-1))
            advanceUntilIdle()

            viewModel.onEvent(CheckOutEvent.CheckoutButton)
            advanceUntilIdle()

            val event = eventDeferred.await()
            assertTrue(event is UiEvent.ShowSnackBar)
            assertEquals(CustomerOrAddressNotFound, event.message)
            assertFalse(viewModel.checkOutState.value.isCheckingOut)

            eventDeferred.cancel()
            job.cancel()
        }

    @Test
    fun `checkout throw exception when getSelectAddressUseCase throw  `() = runTest {
        val job = activateTestFlow(viewModel.checkOutState)
        val customerId = 1
        val addressId = 1

        viewModel.onEvent(CheckOutEvent.Input.CustomerId(customerId))
        viewModel.onEvent(CheckOutEvent.Input.AddressId(addressId))

        advanceUntilIdle()
        coEvery { getSelectAddressUseCase(addressId) } throws Failures.CacheFailure(errorMessage)

        val eventDeferred = async { viewModel.checkOutEvent.first() }
        viewModel.onEvent(CheckOutEvent.CheckoutButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertFalse(viewModel.checkOutState.value.isCheckingOut)
        eventDeferred.cancel()
        job.cancel()

    }

    @Test
    fun `checkout throw exception when getCartUseCase throw  `() = runTest {
        val job = activateTestFlow(viewModel.checkOutState)
        val customerId = 1
        val addressId = 1
        val customerAddressEntity = mockk<CustomerAddressEntity>()

        viewModel.onEvent(CheckOutEvent.Input.CustomerId(customerId))
        viewModel.onEvent(CheckOutEvent.Input.AddressId(addressId))

        advanceUntilIdle()
        coEvery { getSelectAddressUseCase(addressId) } returns customerAddressEntity
        coEvery { getCartUseCase() } throws Failures.CacheFailure(errorMessage)

        val eventDeferred = async { viewModel.checkOutEvent.first() }
        viewModel.onEvent(CheckOutEvent.CheckoutButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertFalse(viewModel.checkOutState.value.isCheckingOut)
        eventDeferred.cancel()
        job.cancel()

    }

    @Test
    fun `checkout throw exception when createOrderUseCase throw  `() = runTest {
        val job = activateTestFlow(viewModel.checkOutState)
        val customerId = 1
        val addressId = 1
        val customerAddressEntity = mockk<CustomerAddressEntity>()
        val cartWithItems = mockk<CartWithItems>()
        val mockBillingInfo = mockk<BillingInfoRequestEntity>(relaxed = true)
        val mockLineItem = listOf(mockk<LineItemRequestEntity>(relaxed = true))

        viewModel.onEvent(CheckOutEvent.Input.CustomerId(customerId))
        viewModel.onEvent(CheckOutEvent.Input.AddressId(addressId))

        advanceUntilIdle()
        coEvery { getSelectAddressUseCase(addressId) } returns customerAddressEntity
        coEvery { getCartUseCase() } returns cartWithItems
        every { AddressMapper.mapCustomerAddressEntityToBillingInfoRequest(customerAddressEntity) } returns mockBillingInfo
        every { ItemMapper.mapCartWithItemsToLineItemRequest(cartWithItems) } returns mockLineItem
        coEvery { createOrderUseCase(any()) } throws Failures.ServerFailure(errorMessage)


        val eventDeferred = async { viewModel.checkOutEvent.first() }
        viewModel.onEvent(CheckOutEvent.CheckoutButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertFalse(viewModel.checkOutState.value.isCheckingOut)
        eventDeferred.cancel()
        job.cancel()

    }

    @Test
    fun `checkout throw exception when clearCartUseCase throw  `() = runTest {
        val job = activateTestFlow(viewModel.checkOutState)
        val customerId = 1
        val addressId = 1
        val customerAddressEntity = mockk<CustomerAddressEntity>()
        val cartWithItems = mockk<CartWithItems>()
        val mockBillingInfo = mockk<BillingInfoRequestEntity>(relaxed = true)
        val mockLineItem = listOf(mockk<LineItemRequestEntity>(relaxed = true))
        val response = mockk<OrderResponseEntity>()

        viewModel.onEvent(CheckOutEvent.Input.CustomerId(customerId))
        viewModel.onEvent(CheckOutEvent.Input.AddressId(addressId))

        advanceUntilIdle()
        coEvery { getSelectAddressUseCase(addressId) } throws Failures.ServerFailure(errorMessage)
        coEvery { getCartUseCase() } returns cartWithItems
        every { AddressMapper.mapCustomerAddressEntityToBillingInfoRequest(customerAddressEntity) } returns mockBillingInfo
        every { ItemMapper.mapCartWithItemsToLineItemRequest(cartWithItems) } returns mockLineItem
        coEvery { createOrderUseCase(any()) } returns response
        coEvery { clearCartUseCase() } throws Failures.CacheFailure(errorMessage)


        val eventDeferred = async { viewModel.checkOutEvent.first() }
        viewModel.onEvent(CheckOutEvent.CheckoutButton)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertFalse(viewModel.checkOutState.value.isCheckingOut)
        eventDeferred.cancel()
        job.cancel()

    }


}