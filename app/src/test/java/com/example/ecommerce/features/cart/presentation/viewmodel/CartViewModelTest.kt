package com.example.ecommerce.features.cart.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.await
import com.example.ecommerce.features.cart.domain.use_case.add_item.IAddItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.get_cart.IGetCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.remove_Item.IRemoveItemUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_item_cart.IUpdateItemsCartUseCase
import com.example.ecommerce.features.cart.domain.use_case.update_quantity.IUpdateQuantityUseCase
import com.example.ecommerce.features.cart.dummyAddItemRequestEntity
import com.example.ecommerce.features.cart.dummyCartWithItemEntity
import com.example.ecommerce.features.cart.keyItem
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.observerViewModelErrorState
import com.example.ecommerce.features.observerViewModelSuccessState
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
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var addItemUseCase: IAddItemUseCase

    @Mock
    private lateinit var getCartUseCase: IGetCartUseCase

    @Mock
    private lateinit var removeItemUseCase: IRemoveItemUseCase

    @Mock
    private lateinit var updateItemsCartUseCase: IUpdateItemsCartUseCase

    @Mock
    private lateinit var updateQuantityUseCase: IUpdateQuantityUseCase
    private lateinit var viewModel: CartViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = CartViewModel(
            addItemUseCase = addItemUseCase,
            getCartUseCase = getCartUseCase,
            removeItemUseCase = removeItemUseCase,
            updateItemsCartUseCase = updateItemsCartUseCase,
            updateQuantityUseCase = updateQuantityUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun cartStateUiAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.cartState.asLiveData()
    }

    @Test
    fun `addItem should emit cartState with success state when use case succeeds`() = runTest {
        val latch = CountDownLatch(1)
        `when`(addItemUseCase(addItemParams = dummyAddItemRequestEntity)).thenReturn(Unit)
        viewModel.addItem(addItemParams = dummyAddItemRequestEntity)
        val observer = observerViewModelSuccessState(
            latch = latch,
            Unit,
            cartStateUiAsLiveData()
        )
        await(latch = latch)
        verify(addItemUseCase).invoke(addItemParams = dummyAddItemRequestEntity)
        removeObserverFromLiveData(cartStateUiAsLiveData(), observer)
    }

    @Test
    fun `addItem should emit cartState with error state when use case throw exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(addItemUseCase(addItemParams = dummyAddItemRequestEntity)).thenThrow(
            RuntimeException(errorMessage)
        )
        viewModel.addItem(addItemParams = dummyAddItemRequestEntity)
        val observer = observerViewModelErrorState(
            latch = latch,
            errorMessage,
            cartStateUiAsLiveData()
        )
        await(latch = latch)
        verify(addItemUseCase).invoke(addItemParams = dummyAddItemRequestEntity)
        removeObserverFromLiveData(cartStateUiAsLiveData(), observer)
    }

    @Test
    fun `getCart should emit cartState with success state when use case succeeds`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getCartUseCase()).thenReturn(dummyCartWithItemEntity)
        viewModel.getCart()
        val observer = observerViewModelSuccessState(
            latch = latch,
            dummyCartWithItemEntity,
            cartStateUiAsLiveData()
        )
        await(latch = latch)
        verify(getCartUseCase).invoke()
        removeObserverFromLiveData(cartStateUiAsLiveData(), observer)
    }

    @Test
    fun `getCart should emit cartState with error state when use case throw exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getCartUseCase()).thenThrow(
            RuntimeException(errorMessage)
        )
        viewModel.getCart()
        val observer = observerViewModelErrorState(
            latch = latch,
            errorMessage,
            cartStateUiAsLiveData()
        )
        await(latch = latch)
        verify(getCartUseCase).invoke()
        removeObserverFromLiveData(cartStateUiAsLiveData(), observer)
    }

    @Test
    fun `removeItem should emit cartState with success state when use case succeeds`() = runTest {
        val latch = CountDownLatch(1)
        `when`(removeItemUseCase(keyItem = keyItem)).thenReturn(Unit)
        viewModel.removeItem(keyItem = keyItem)
        val observer = observerViewModelSuccessState(
            latch = latch,
            Unit,
            cartStateUiAsLiveData()
        )
        await(latch = latch)
        verify(removeItemUseCase).invoke(keyItem = keyItem)
        removeObserverFromLiveData(cartStateUiAsLiveData(), observer)
    }

    @Test
    fun `removeItem should emit cartState with error state when use case throw exception`() =
        runTest {
            val latch = CountDownLatch(1)
            `when`(removeItemUseCase(keyItem = keyItem)).thenThrow(
                RuntimeException(errorMessage)
            )
            viewModel.removeItem(keyItem = keyItem)
            val observer = observerViewModelErrorState(
                latch = latch,
                errorMessage,
                cartStateUiAsLiveData()
            )
            await(latch = latch)
            verify(removeItemUseCase).invoke(keyItem = keyItem)
            removeObserverFromLiveData(cartStateUiAsLiveData(), observer)
        }

}