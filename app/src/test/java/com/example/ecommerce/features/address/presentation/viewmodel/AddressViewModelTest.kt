package com.example.ecommerce.features.address.presentation.viewmodel

import com.example.ecommerce.activateTestFlow
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.features.MainDispatcherRule
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity
import com.example.ecommerce.features.address.domain.usecases.getaddress.GetAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.insertupdateaddress.InsertAddressUseCase
import com.example.ecommerce.features.address.domain.usecases.updateaddress.UpdateAddressUseCase
import com.example.ecommerce.features.address.presentation.event.AddressEvent
import com.example.ecommerce.features.address.presentation.viewmodel.address.AddressViewModel
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
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExperimentalCoroutinesApi
class AddressViewModelTest {
    @get:Rule
    val mainDispatcher = MainDispatcherRule()
    private val updateAddressUseCase = mockk<UpdateAddressUseCase>()
    private val getAddressUseCase = mockk<GetAddressUseCase>()
    private val insertAddressUseCase = mockk<InsertAddressUseCase>()
    private lateinit var viewModel: AddressViewModel

    @Before
    fun setup() {
        viewModel = AddressViewModel(
            updateAddressUseCase = updateAddressUseCase,
            getAddressUseCase = getAddressUseCase,
            insertAddressUseCase = insertAddressUseCase
        )
    }

    private val tAddressRequestEntity = mockk<AddressRequestEntity>()
    private val tAddressResponseEntity = mockk<UpdateAddressResponseEntity>()
    private val customerEntity = mockk<CustomerAddressEntity>()
    private val tAddressId = 10

    companion object {
        private const val UPDATE_ADDRESS = "updateAddress"
        private const val GET_ADDRESS = "getAllAddress"
        private const val INSERT_ADDRESS = "insertAddress"
    }

    @Test
    fun `onEvent updateAddress should update the Input Value`() = runTest {
        val job = activateTestFlow(viewModel.addressState)
        viewModel.onEvent(AddressEvent.Input.UpdateAddress(tAddressId, tAddressRequestEntity))
        val state = viewModel.addressState.value
        assert(state.id == tAddressId)
        assert(state.addressRequestEntity == tAddressRequestEntity)
        job.cancel()
    }

    @Test
    fun `onEvent updateAddress button should trigger the updateAddressUseCase`() = runTest {
        val addressSpy = spyk(viewModel, recordPrivateCalls = true)
        addressSpy.onEvent(AddressEvent.Button.UpdateAddressButton)
        coVerify(exactly = 1) { addressSpy[UPDATE_ADDRESS]() }
    }

    @Test
    fun `onEvent insertAddress button should trigger the insertAddressUseCase`() = runTest {
        val addressSpy = spyk(viewModel, recordPrivateCalls = true)
        addressSpy.onEvent(AddressEvent.Button.InsertAddressButton)
        coVerify(exactly = 1) { addressSpy[INSERT_ADDRESS]() }
    }

    @Test
    fun `onEvent getAddress button should trigger the getAddressUseCase`() = runTest {
        val addressSpy = spyk(viewModel, recordPrivateCalls = true)
        addressSpy.onEvent(AddressEvent.LoadAllAddress)
        coVerify(exactly = 1) { addressSpy[GET_ADDRESS]() }
    }

    @Test
    fun `updateAddress should update the address when id bigger than -1`() = runTest {
        viewModel.onEvent(AddressEvent.Input.UpdateAddress(tAddressId, tAddressRequestEntity))
        advanceUntilIdle()
        val id = viewModel.addressState.value.id
        val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
        coEvery {
            updateAddressUseCase.invoke(
                id,
                addressRequestEntity
            )
        } returns tAddressResponseEntity

        val channelEvents = mutableListOf<UiEvent>()
        val collectJob = async {
            viewModel.addressEvent.collect {
                channelEvents.add(it)
            }
        }
        viewModel.onEvent(AddressEvent.Button.UpdateAddressButton)
        advanceUntilIdle()

        coVerify(exactly = 1) { updateAddressUseCase.invoke(id, addressRequestEntity) }

        assertTrue(channelEvents.isNotEmpty())
        val combinedEvent = channelEvents.first()
        assertTrue(combinedEvent is UiEvent.CombinedEvents)
        val eventsInCombined = (combinedEvent as UiEvent.CombinedEvents).events
        assertEquals(2, eventsInCombined.size)
        assertTrue(eventsInCombined.any { it is UiEvent.ShowSnackBar })
        assertTrue(eventsInCombined.any { it is UiEvent.Navigation.Address })

        collectJob.cancel()
        assertFalse(viewModel.addressState.value.isUpdateLoading)
    }

    @Test
    fun ` updateAddress should not trigger the updateAddressUseCase when id is -1`() =
        runTest {
            viewModel.onEvent(AddressEvent.Input.UpdateAddress(-1, tAddressRequestEntity))
            advanceUntilIdle()
            val id = viewModel.addressState.value.id
            val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
            viewModel.onEvent(AddressEvent.Button.UpdateAddressButton)
            advanceUntilIdle()
            assertFalse(viewModel.addressState.value.isUpdateLoading)
            coVerify(exactly = 0) {
                updateAddressUseCase.invoke(id, addressRequestEntity)
            }
        }

    @Test
    fun `updateAddress throw Failure Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(AddressEvent.Input.UpdateAddress(tAddressId, tAddressRequestEntity))
        advanceUntilIdle()
        val id = viewModel.addressState.value.id
        val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
        coEvery {
            updateAddressUseCase.invoke(
                id,
                addressRequestEntity
            )
        } throws Failures.ServerFailure(errorMessage)
        val eventDeferred = async { viewModel.addressEvent.first() }
        viewModel.onEvent(AddressEvent.Button.UpdateAddressButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.addressState.value.isUpdateLoading)
    }

    @Test
    fun `updateAddress throw Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(AddressEvent.Input.UpdateAddress(tAddressId, tAddressRequestEntity))
        advanceUntilIdle()
        val id = viewModel.addressState.value.id
        val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
        coEvery {
            updateAddressUseCase.invoke(
                id,
                addressRequestEntity
            )
        } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.addressEvent.first() }
        viewModel.onEvent(AddressEvent.Button.UpdateAddressButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.addressState.value.isUpdateLoading)
    }

    @Test
    fun `insertAddress should insert the address value into the database`() = runTest {
        viewModel.onEvent(AddressEvent.Input.InsertAddress(tAddressRequestEntity))
        advanceUntilIdle()
        val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
        coEvery {
            insertAddressUseCase.invoke(
                addressRequestEntity
            )
        } just Runs

        val channelEvents = mutableListOf<UiEvent>()
        val collectJob = async {
            viewModel.addressEvent.collect {
                channelEvents.add(it)
            }
        }
        viewModel.onEvent(AddressEvent.Button.InsertAddressButton)
        advanceUntilIdle()

        coVerify(exactly = 1) { insertAddressUseCase.invoke(addressRequestEntity) }
        assertTrue(channelEvents.isNotEmpty())
        val combinedEvent = channelEvents.first()
        assertTrue(combinedEvent is UiEvent.CombinedEvents)
        val eventsInCombined = (combinedEvent as UiEvent.CombinedEvents).events
        assertEquals(2, eventsInCombined.size)
        assertTrue(eventsInCombined.any { it is UiEvent.ShowSnackBar })
        assertTrue(eventsInCombined.any { it is UiEvent.Navigation.Address })

        collectJob.cancel()
        assertFalse(viewModel.addressState.value.isInsertLoading)
    }

    @Test
    fun `insertAddress throw Failure Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(AddressEvent.Input.InsertAddress(tAddressRequestEntity))
        advanceUntilIdle()

        val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
        coEvery {
            insertAddressUseCase.invoke(
                addressRequestEntity
            )
        } throws Failures.ServerFailure(errorMessage)
        val eventDeferred = async { viewModel.addressEvent.first() }
        viewModel.onEvent(AddressEvent.Button.InsertAddressButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.addressState.value.isInsertLoading)
    }

    @Test
    fun `insertAddress throw Exception should send ShowSnackBar event`() = runTest {
        viewModel.onEvent(AddressEvent.Input.InsertAddress(tAddressRequestEntity))
        advanceUntilIdle()
        val addressRequestEntity = viewModel.addressState.value.addressRequestEntity
        coEvery {
            insertAddressUseCase.invoke(
                addressRequestEntity
            )
        } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.addressEvent.first() }
        viewModel.onEvent(AddressEvent.Button.InsertAddressButton)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.addressState.value.isInsertLoading)
    }

    @Test
    fun `getAllAddress should call getAllAddressUseCase`() = runTest {
        val tCustomers = listOf(customerEntity)
        coEvery {
            getAddressUseCase.invoke()
        } returns tCustomers
        viewModel.onEvent(AddressEvent.LoadAllAddress)
        advanceUntilIdle()
        coVerify(exactly = 1) { getAddressUseCase.invoke() }
        assertEquals(tCustomers, viewModel.addressState.value.addressList)
        assertFalse(viewModel.addressState.value.isGetAllAddressLoading)

    }

    @Test
    fun `getAllAddress throw Failure should send ShowSnackBar event`() = runTest {
        coEvery {
            getAddressUseCase.invoke()

        } throws Failures.CacheFailure(errorMessage)
        val eventDeferred = async { viewModel.addressEvent.first() }
        viewModel.onEvent(AddressEvent.LoadAllAddress)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.addressState.value.isGetAllAddressLoading)
    }

    @Test
    fun `getAllAddress throw Exception should send ShowSnackBar event`() = runTest {
        coEvery {
            getAddressUseCase.invoke()

        } throws Exception(errorMessage)
        val eventDeferred = async { viewModel.addressEvent.first() }
        viewModel.onEvent(AddressEvent.LoadAllAddress)
        advanceUntilIdle()
        val event = eventDeferred.await()
        assertTrue(event is UiEvent.ShowSnackBar)
        assertEquals(errorMessage, (event as UiEvent.ShowSnackBar).message)
        assertFalse(viewModel.addressState.value.isGetAllAddressLoading)
    }
}