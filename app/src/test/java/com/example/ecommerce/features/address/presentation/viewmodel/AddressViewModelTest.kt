package com.example.ecommerce.features.address.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ecommerce.core.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.domain.usecases.checkupdateaddress.ICheckUpdateAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.getaddressbyid.IGetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.IUpdateAddressUseCase
import com.example.ecommerce.features.await
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
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
    private lateinit var checkUpdateAddressUseCase: ICheckUpdateAddressUseCase
    private lateinit var viewModel: AddressViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = AddressViewModel(
            updateAddressUseCase,
            getAddressUseCase,
            checkUpdateAddressUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun addressStateAsLiveData(): LiveData<UiState<Any>> {
        return viewModel.addressState.asLiveData()
    }

    private val tShippingInfoRequestEntity = ShippingInfoRequestEntity(
        firstName = "John",
        lastName = "Doe",
        address = "123 Main St",
        city = "Springfield",
        state = "IL",
        postCode = "62701",
        country = "US",
    )
    private val tBillingInfoRequestEntity = BillingInfoRequestEntity(
        firstName = "John",
        lastName = "Doe",
        address = "123 Main St",
        city = "Springfield",
        state = "IL",
        postCode = "62701",
        country = "US",
        email = "billing@example.com",
        phone = "555-555-5555"
    )
    private val tAddressRequestEntity = AddressRequestEntity(
        billing = tBillingInfoRequestEntity,
        shipping = tShippingInfoRequestEntity
    )
    private val tCustomerAddressEntity = CustomerAddressEntity(
        id = 0,
        userId = 1,
        firstName = "John",
        lastName = "Doe",
        address = "123 Main St",
        city = "Springfield",
        state = "IL",
        zipCode = "62701",
        country = "US",
        email = "billing@example.com",
        phone = "555-555-5555"
    )
    private val errorMessage = "Error message"

    @Test
    fun `updateAddress should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        viewModel.updateAddress(updateAddressParams = tAddressRequestEntity)
        val observer = observerViewModelSuccessState(
            latch,
            Unit,
            addressStateAsLiveData()
        )
        verify(updateAddressUseCase).invoke(customerAddressParams = tAddressRequestEntity)
        await(latch = latch)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `updateAddress should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        whenever(updateAddressUseCase.invoke(customerAddressParams = tAddressRequestEntity))
            .thenThrow(RuntimeException(errorMessage))
        viewModel.updateAddress(updateAddressParams = tAddressRequestEntity)
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        verify(updateAddressUseCase).invoke(customerAddressParams = tAddressRequestEntity)
        await(latch = latch)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }
    @Test
    fun `getAddressById should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        whenever(getAddressUseCase.invoke(id = 1)).thenReturn(
            tCustomerAddressEntity
        )
        viewModel.getAddressById(1)
        val observer = observerViewModelSuccessState(
            latch,
            tCustomerAddressEntity,
            addressStateAsLiveData()
        )
        verify(getAddressUseCase).invoke(id = 1)
        await(latch = latch)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `getAddressById should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        whenever(getAddressUseCase.invoke(id = 1))
            .thenThrow(RuntimeException(errorMessage))
        viewModel.getAddressById(1)
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        verify(getAddressUseCase).invoke(id = 1)
        await(latch = latch)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }
    @Test
    fun `checkUpdateAddress should emit success state when use case returns data`() = runTest {
        val latch = CountDownLatch(1)
        whenever(checkUpdateAddressUseCase.invoke()).thenReturn(tCustomerAddressEntity)
        viewModel.checkUpdateAddress()
        val observer = observerViewModelSuccessState(
            latch,
            tCustomerAddressEntity,
            addressStateAsLiveData()
        )
        verify(checkUpdateAddressUseCase).invoke()
        await(latch = latch)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }

    @Test
    fun `checkUpdateAddress should emit error state when use case throws exception`() = runTest {
        val latch = CountDownLatch(1)
        whenever(checkUpdateAddressUseCase.invoke())
            .thenThrow(RuntimeException(errorMessage))
        viewModel.checkUpdateAddress()
        val observer = observerViewModelErrorState(
            latch,
            errorMessage,
            addressStateAsLiveData()
        )
        verify(checkUpdateAddressUseCase).invoke()
        await(latch = latch)
        removeObserverFromLiveData(addressStateAsLiveData(), observer)

    }


}