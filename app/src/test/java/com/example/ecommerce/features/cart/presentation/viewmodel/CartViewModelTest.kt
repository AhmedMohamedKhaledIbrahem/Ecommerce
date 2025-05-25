package com.example.ecommerce.features.cart.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.domain.use_case.add_item.IAddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart_count.IGetCartCountUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.IRemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_quantity.IUpdateQuantityUseCase
import com.example.ecommerce.features.cart.presentation.event.CartEvent
import com.example.ecommerce.features.errorMessage
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class CartViewModelTest {
    @get:Rule
    val mainDispatcher = MainDispatcherRule()

    private val addItemUseCase = mockk<IAddItemUseCase>()
    private val getCartUseCase = mockk<IGetCartUseCase>()
    private val removeItemUseCase = mockk<IRemoveItemUseCase>()
    private val updateQuantityUseCase = mockk<IUpdateQuantityUseCase>()
    private val getCartCountUseCase = mockk<IGetCartCountUseCase>()
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        viewModel = CartViewModel(
            addItemUseCase = addItemUseCase,
            getCartUseCase = getCartUseCase,
            removeItemUseCase = removeItemUseCase,
            updateQuantityUseCase = updateQuantityUseCase,
            getCartCountUseCase = getCartCountUseCase
        )
    }

    private val getCart = "getCart"
    private val addItem = "addItem"
    private val removeItem = "removeItem"
    private val decreaseQuantity = "decreaseQuantity"
    private val increaseQuantity = "increaseQuantity"

    @Test
    fun `onEvent LoadCart should call getCartUseCase`() = runTest {
        val cartSpy = spyk(viewModel, recordPrivateCalls = true)
        cartSpy.onEvent(CartEvent.LoadCart)
        coVerify(exactly = 1) { cartSpy[getCart]() }
    }

    @Test
    fun `onEvent Input AddItem should update addItemParams in state`() = runTest {
        val job = activateTestFlow(viewModel.cartState)
        val addItemParams = mockk<AddItemRequestEntity>()
        viewModel.onEvent(CartEvent.Input.AddItem(addItemParams))
        advanceUntilIdle()
        assertEquals(addItemParams, viewModel.cartState.value.addItemParams)
        job.cancel()
    }

    @Test
    fun `onEvent Input ItemId should update itemId in state`() = runTest {
        val job = activateTestFlow(viewModel.cartState)
        val itemId = 123
        viewModel.onEvent(CartEvent.Input.ItemId(itemId))
        advanceUntilIdle()
        assertEquals(itemId, viewModel.cartState.value.itemId)
        job.cancel()
    }

    @Test
    fun `onEvent Input IncreaseQuantity should update increase in state`() = runTest {
        val job = activateTestFlow(viewModel.cartState)
        val increase = 1
        viewModel.onEvent(CartEvent.Input.IncreaseQuantity(increase))
        advanceUntilIdle()
        assertEquals(increase, viewModel.cartState.value.increase)
        job.cancel()
    }

    @Test
    fun `onEvent Input DecreaseQuantity should update decrease in state`() = runTest {
        val job = activateTestFlow(viewModel.cartState)
        val decrease = 1
        viewModel.onEvent(CartEvent.Input.DecreaseQuantity(decrease))
        advanceUntilIdle()
        assertEquals(decrease, viewModel.cartState.value.decrease)
        job.cancel()
    }

    @Test
    fun `onEvent Input RemoveItem should update removeItem in state`() = runTest {
        val job = activateTestFlow(viewModel.cartState)
        val keyItem = "key"
        viewModel.onEvent(CartEvent.Input.RemoveItem(keyItem))
        advanceUntilIdle()
        assertEquals(keyItem, viewModel.cartState.value.removeItem)
        job.cancel()
    }

    @Test
    fun `onEvent Button IncreaseQuantity should call updateQuantityUseCase`() = runTest {
        val cartSpy = spyk(viewModel, recordPrivateCalls = true)
        cartSpy.onEvent(CartEvent.Button.IncreaseQuantity)
        coVerify(exactly = 1) { cartSpy[increaseQuantity]() }
    }

    @Test
    fun `onEvent Button DecreaseQuantity should call updateQuantityUseCase`() = runTest {
        val cartSpy = spyk(viewModel, recordPrivateCalls = true)
        cartSpy.onEvent(CartEvent.Button.DecreaseQuantity)
        coVerify(exactly = 1) { cartSpy[decreaseQuantity]() }
    }

    @Test
    fun `onEvent Button AddToCart should call addItemUseCase`() = runTest {
        val cartSpy = spyk(viewModel, recordPrivateCalls = true)
        cartSpy.onEvent(CartEvent.Button.AddToCart)
        coVerify(exactly = 1) { cartSpy[addItem]() }
    }

    @Test
    fun `onEvent Button RemoveItem should call removeItemUseCase`() = runTest {
        val cartSpy = spyk(viewModel, recordPrivateCalls = true)
        cartSpy.onEvent(CartEvent.Button.RemoveItem)
        coVerify(exactly = 1) { cartSpy[removeItem]() }
    }

    @Test
    fun `getCart should call getCartUseCase and update cartState`() = runTest {
        val cartWithItems = mockk<CartWithItems>()
        coEvery { getCartUseCase.invoke() } returns cartWithItems
        viewModel.onEvent(CartEvent.LoadCart)
        advanceUntilIdle()
        coVerify(exactly = 1) { getCartUseCase.invoke() }
        assertEquals(cartWithItems, viewModel.cartState.value.cartWithItems)
        assertFalse(viewModel.cartLoadState.value.isGetLoading)
    }

    @Test
    fun `getCart should catch Failures and send ShowSnackBar event`() = runTest {
        coEvery { getCartUseCase.invoke() } throws Failures.ServerFailure(errorMessage)
        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.LoadCart)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.cartLoadState.value.isGetLoading)
    }

    @Test
    fun `getCart should catch Exception and send ShowSnackBar event`() = runTest {
        coEvery { getCartUseCase.invoke() } throws Exception(errorMessage)

        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.LoadCart)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.cartLoadState.value.isGetLoading)
    }

    @Test
    fun `increaseQuantity should call updateQuantityUseCase`() = runTest {
        val (itemId, increase) = Pair<Int, Int>(1, 1)
        viewModel.onEvent(CartEvent.Input.ItemId(itemId))
        viewModel.onEvent(CartEvent.Input.IncreaseQuantity(increase))

        coEvery {
            updateQuantityUseCase.invoke(
                viewModel.cartState.value.itemId,
                viewModel.cartState.value.increase
            )
        } just Runs
        viewModel.onEvent(CartEvent.Button.IncreaseQuantity)
        advanceUntilIdle()
        coVerify(exactly = 1) {
            updateQuantityUseCase.invoke(
                viewModel.cartState.value.itemId,
                viewModel.cartState.value.increase
            )
        }
    }

    @Test
    fun `increaseQuantity should catch Failures and send ShowSnackBar event`() =
        runTest {
            coEvery { updateQuantityUseCase.invoke(any(), any()) } throws Failures.ServerFailure(
                errorMessage
            )

            val eventDeferred = async { viewModel.cartEvent.first() }
            viewModel.onEvent(CartEvent.Button.IncreaseQuantity)
            advanceUntilIdle()
            val event = eventDeferred.await()

            assertTrue(event is UiEvent.ShowSnackBar)
            assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        }

    @Test
    fun `increaseQuantity should catch Exception and send ShowSnackBar event`() = runTest {
        coEvery { updateQuantityUseCase.invoke(any(), any()) } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.IncreaseQuantity)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
    }

    @Test
    fun `decreaseQuantity should call updateQuantityUseCase`() = runTest {
        val (itemId, increase) = Pair<Int, Int>(1, 1)
        viewModel.onEvent(CartEvent.Input.ItemId(itemId))
        viewModel.onEvent(CartEvent.Input.DecreaseQuantity(increase))

        coEvery {
            updateQuantityUseCase.invoke(
                viewModel.cartState.value.itemId,
                viewModel.cartState.value.decrease
            )
        } just Runs
        viewModel.onEvent(CartEvent.Button.DecreaseQuantity)
        advanceUntilIdle()
        coVerify(exactly = 1) {
            updateQuantityUseCase.invoke(
                viewModel.cartState.value.itemId,
                viewModel.cartState.value.decrease
            )
        }
    }

    @Test
    fun `decreaseQuantity should catch Failures and send ShowSnackBar event`() = runTest {
        coEvery { updateQuantityUseCase.invoke(any(), any()) } throws Failures.ServerFailure(
            errorMessage
        )

        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.DecreaseQuantity)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `decreaseQuantity should catch Exception and send ShowSnackBar event`() = runTest {
        coEvery { updateQuantityUseCase.invoke(any(), any()) } throws Exception(
            errorMessage
        )

        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.DecreaseQuantity)
        advanceUntilIdle()

        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)

    }

    @Test
    fun `addItem should call addItemUseCase to add item success then send ShowSnackBar event and navigate to cart`() =
        runTest {
            val addItemParams = mockk<AddItemRequestEntity>()
            viewModel.onEvent(CartEvent.Input.AddItem(addItemParams))
            coEvery { addItemUseCase.invoke(addItemParams) } just Runs

            val events = mutableListOf<UiEvent>()
            val collectJob = async { viewModel.cartEvent.collect { events.add(it) } }
            viewModel.onEvent(CartEvent.Button.AddToCart)
            advanceUntilIdle()

            coVerify(exactly = 1) { addItemUseCase.invoke(addItemParams) }

            assertTrue(events.isNotEmpty())
            val combinedEvent = events.first()
            assertTrue(combinedEvent is UiEvent.CombinedEvents)
            val eventsInCombined = (combinedEvent as UiEvent.CombinedEvents).events
            assertEquals(2, eventsInCombined.size)
            assertTrue(eventsInCombined.any { it is UiEvent.ShowSnackBar })
            assertTrue(eventsInCombined.any { it is UiEvent.Navigation.Cart })

            collectJob.cancel()
            assertFalse(viewModel.cartLoadState.value.isAddLoading)
        }

    @Test
    fun `AddItem should catch Failures and send ShowSnackBar event`() = runTest {
        coEvery { addItemUseCase.invoke(any()) } throws Failures.ServerFailure(errorMessage)
        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.AddToCart)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.cartLoadState.value.isAddLoading)
    }

    @Test
    fun `AddItem should catch Exception and send ShowSnackBar event`() = runTest {
        coEvery { addItemUseCase.invoke(any()) } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.AddToCart)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.cartLoadState.value.isAddLoading)
    }

    @Test
    fun `removeItem should call removeItemUseCase to remove item success then send ShowSnackBar event`() =
        runTest {
            val keyItem = "key"
            val errorShowMessage = "Item removed successfully"
            viewModel.onEvent(CartEvent.Input.RemoveItem(keyItem))
            coEvery { removeItemUseCase.invoke(viewModel.cartState.value.removeItem) } just Runs

            val eventDeferred = async { viewModel.cartEvent.first() }
            viewModel.onEvent(CartEvent.Button.RemoveItem)
            advanceUntilIdle()
            coVerify(exactly = 1) { removeItemUseCase.invoke(viewModel.cartState.value.removeItem) }
            val event = eventDeferred.await()
            assertTrue(event is UiEvent.ShowSnackBar)
            assertEquals(errorShowMessage, (event as UiEvent.ShowSnackBar).message)
            assertFalse(viewModel.cartLoadState.value.isRemoveLoading)
        }

    @Test
    fun `removeItem should catch Failures and send ShowSnackBar event`() = runTest {

        coEvery { removeItemUseCase.invoke(any()) } throws Failures.ServerFailure(errorMessage)
        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.RemoveItem)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.cartLoadState.value.isRemoveLoading)
    }

    @Test
    fun `removeItem should catch Exception and send ShowSnackBar event`() = runTest {
        coEvery { removeItemUseCase.invoke(any()) } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.cartEvent.first() }
        viewModel.onEvent(CartEvent.Button.RemoveItem)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.cartLoadState.value.isRemoveLoading)
    }


}