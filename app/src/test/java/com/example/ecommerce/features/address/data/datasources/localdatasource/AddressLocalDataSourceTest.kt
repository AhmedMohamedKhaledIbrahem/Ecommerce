package com.example.ecommerce.features.address.data.datasources.localdatasource

import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AddressLocalDataSourceTest {

    @Mock
    private lateinit var addressDao: AddressDao
    private lateinit var localDataSource: AddressLocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        localDataSource = AddressLocalDataSourceImp(dao = addressDao)
    }

    private val tAddressId = 1
    private val tCustomerAddressEntity = CustomerAddressEntity(
        id = 0,
        userId = tAddressId,
        firstName = "Old",
        lastName = "Name",
        address = "Old Address",
        city = "Old City",
        state = "Old State",
        zipCode = "0000",
        country = "Old Country",
        email = "old@example.com",
        phone = "000000"
    )
    private val updateAddressParams = fixture("address.json").run {
        Gson().fromJson(this, AddressResponseModel::class.java)
    }
    val tErrorString = "Database error"

    @Test
    fun `UpdateAddressById should update the address in the database when the addressId exists`() =
        runTest {
            whenever(addressDao.getAddressById(tAddressId)).thenReturn(tCustomerAddressEntity)


            localDataSource.updateAddress(updateAddressParams)
            val updatedAddressCaptor = argumentCaptor<CustomerAddressEntity>()
            verify(addressDao).updateAddress(updatedAddressCaptor.capture())

            val updatedAddress = updatedAddressCaptor.firstValue
            assertEquals("John", updatedAddress.firstName)
            assertEquals("Doe", updatedAddress.lastName)
            assertEquals("123 Main St", updatedAddress.address)
            assertEquals("Springfield", updatedAddress.city)
            assertEquals("IL", updatedAddress.state)
            assertEquals("62701", updatedAddress.zipCode)
            assertEquals("US", updatedAddress.country)
            assertEquals("billing@example.com", updatedAddress.email)
            assertEquals("555-555-5555", updatedAddress.phone)

        }

    @Test
    fun `updateAddressById should insert a new address in the database when the addressId not exist`() =
        runTest {
            whenever(addressDao.getAddress(tAddressId)).thenReturn(null)
            localDataSource.updateAddress(updateAddressParams = updateAddressParams)
            val addressCaptor = argumentCaptor<CustomerAddressEntity>()
            verify(addressDao).insertAddress(customerAddressEntity = addressCaptor.capture())
            val insertedAddress = addressCaptor.firstValue
            assertEquals("John", insertedAddress.firstName)
            assertEquals("Doe", insertedAddress.lastName)
            assertEquals("123 Main St", insertedAddress.address)
            assertEquals("Springfield", insertedAddress.city)
            assertEquals("IL", insertedAddress.state)
            assertEquals("62701", insertedAddress.zipCode)
            assertEquals("US", insertedAddress.country)
            assertEquals("billing@example.com", insertedAddress.email)
            assertEquals("555-555-5555", insertedAddress.phone)
        }

    @Test
    fun `updateAddressById should throw a FailureException when an exception occurs`() = runTest {
        whenever(addressDao.getAddressById(tAddressId)).thenThrow(RuntimeException(tErrorString))
        val exception = assertFailsWith<FailureException> {
            localDataSource.updateAddress(updateAddressParams = updateAddressParams)

        }
        assertEquals(tErrorString, exception.message)

    }

    @Test
    fun `getAddressById should return the address when it exists`() = runTest {
        whenever(addressDao.getAddress(tAddressId)).thenReturn(tCustomerAddressEntity)
        val result = localDataSource.getAddressById(tAddressId)
        assertEquals(tCustomerAddressEntity, result)

    }

    @Test
    fun `getAddressById should throw a FailureException when an exception occurs`() = runTest {
        whenever(addressDao.getAddress(tAddressId)).thenThrow(RuntimeException(tErrorString))
        val exception = assertFailsWith<FailureException> {
            localDataSource.getAddressById(tAddressId)
        }
        assertEquals(tErrorString, exception.message)
    }

    @Test
    fun `checkAddressEntityById should return the address when it exists`() = runTest {
        whenever(addressDao.getAddressById(tAddressId)).thenReturn(tCustomerAddressEntity)
        val result = localDataSource.checkAddressEntityById(tAddressId)
        assertEquals(tCustomerAddressEntity, result)

    }

    @Test
    fun `checkAddressEntityById should throw a FailureException when an exception occurs`() = runTest {
        whenever(addressDao.getAddressById(tAddressId)).thenThrow(RuntimeException(tErrorString))
        val exception = assertFailsWith<FailureException> {
            localDataSource.checkAddressEntityById(tAddressId)
        }
        assertEquals(tErrorString, exception.message)
    }

}