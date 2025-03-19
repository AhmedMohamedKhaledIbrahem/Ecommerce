package com.example.ecommerce.features.address.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.address.domain.usecases.deleteaddress.IDeleteAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddress.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.IInsertAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.IUpdateAddressUseCase
import com.example.ecommerce.features.address.id
import com.example.ecommerce.features.address.tAddressRequestEntity
import com.example.ecommerce.features.address.tListCustomerAddressEntity
import com.example.ecommerce.features.address.tUpdateAddressResponseEntity
import com.example.ecommerce.features.await
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
class AddressViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var updateAddressUseCase: IUpdateAddressUseCase

    @Mock
    private lateinit var getAddressUseCase: IGetAddressUseCase

    @Mock
    private lateinit var insertAddressUseCase: IInsertAddressUseCase

    @Mock
    private lateinit var deleteAddressUseCase: IDeleteAddressUseCase
    private lateinit var viewModel: IAddressViewModel
    private val dispatcher = UnconfinedTestDispatcher()
    private val latch = CountDownLatch(1)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = AddressViewModel(
            updateAddressUseCase,
            getAddressUseCase,
            insertAddressUseCase,
            deleteAddressUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun addressStateAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.addressState.asLiveData()
    }


    @Test
    fun `updateAddress should emit success state when use case returns data`() = runTest {
        `when`(
            updateAddressUseCase(
                id = id,
                customerAddressParams = tAddressRequestEntity
            )
        ).thenReturn(
            tUpdateAddressResponseEntity
        )
        viewModel.updateAddress(id = id, updateAddressParams = tAddressRequestEntity)
        val observer = observerViewModelSuccessState(
            latch = latch,
            Unit,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(updateAddressUseCase).invoke(id, customerAddressParams = tAddressRequestEntity)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `updateAddress should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(updateAddressUseCase.invoke(id =id,customerAddressParams = tAddressRequestEntity))
            .thenThrow(RuntimeException(errorMessage))
        viewModel.updateAddress(id = id,updateAddressParams = tAddressRequestEntity)
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(updateAddressUseCase).invoke(id = id,customerAddressParams = tAddressRequestEntity)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }
    @Test
    fun `getAddress should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getAddressUseCase.invoke()).thenReturn(
            tListCustomerAddressEntity
        )
        viewModel.getAddress()
        val observer = observerViewModelSuccessState(
            latch,
            tListCustomerAddressEntity,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(getAddressUseCase).invoke()
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `getAddress should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(getAddressUseCase.invoke())
            .thenThrow(RuntimeException(errorMessage))
        viewModel.getAddress()
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(getAddressUseCase).invoke()
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `insertAddress should emit success state when use case insert address data`() = runTest {
        val latch = CountDownLatch(1)
        `when`(insertAddressUseCase.invoke(tAddressRequestEntity)).thenReturn(
            Unit
        )
        viewModel.insertAddress(tAddressRequestEntity)
        val observer = observerViewModelSuccessState(
            latch,
            Unit,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(insertAddressUseCase).invoke(tAddressRequestEntity)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `insertAddress should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(insertAddressUseCase.invoke(tAddressRequestEntity))
            .thenThrow(RuntimeException(errorMessage))
        viewModel.insertAddress(tAddressRequestEntity)
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(insertAddressUseCase).invoke(tAddressRequestEntity)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }
    @Test
    fun `deleteAddress should emit success state when use case delete All address data`() = runTest {
        val latch = CountDownLatch(1)
        `when`(deleteAddressUseCase.invoke()).thenReturn(
            Unit
        )
        viewModel.deleteAddress()
        val observer = observerViewModelSuccessState(
            latch,
            Unit,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(deleteAddressUseCase).invoke()
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `deleteAddress should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        `when`(deleteAddressUseCase.invoke())
            .thenThrow(RuntimeException(errorMessage))
        viewModel.deleteAddress()
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        await(latch = latch)
        verify(deleteAddressUseCase).invoke()
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }
}