package com.example.ecommerce.features.address.data.repositories

import com.example.ecommerce.core.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.address.data.datasources.localdatasource.AddressLocalDataSource
import com.example.ecommerce.features.address.data.datasources.remotedatasource.AddressRemoteDataSource
import com.example.ecommerce.features.address.data.mapper.AddressMapper
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AddressRepositoryTest {
    @Mock
    private lateinit var remoteDataSource: AddressRemoteDataSource

    @Mock
    private lateinit var localDataSource: AddressLocalDataSource

    @Mock
    private lateinit var internetConnectionChecker: InternetConnectionChecker

    private lateinit var repository: AddressRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = AddressRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
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
    private val tAddressRequestEntity = AddressRequestEntity(
        billing = tBillingInfoRequestEntity,
        shipping = tShippingInfoRequestEntity
    )
    private val tAddressRequestModel = AddressMapper.mapToModel(entity = tAddressRequestEntity)
    private val tAddressResponseModel = fixture("address.json").run {
        Gson().fromJson(this, AddressResponseModel::class.java)
    }
    private val tCheckUpdateAddressResponseModel = fixture(" checkUpdateAddress.json").run {
        Gson().fromJson(this, CheckUpdateAddressResponseModel::class.java)
    }

    private val tErrorMessage = "error message"

    @Test
    fun `updateAddress Should update address when internet connection is available`() = runTest {
        whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
        whenever(remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel))
            .thenReturn(tAddressResponseModel)
        repository.updateAddress(customerAddressParams = tAddressRequestEntity)
        verify(localDataSource).updateAddress(updateAddressParams = tAddressResponseModel)
    }

    @Test
    fun `updateAddress Should throw connection failure when internet connection is not available`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.updateAddress(customerAddressParams = tAddressRequestEntity)

            }
            assert(exception.message == "No Internet Connection")
            verifyNoInteractions(remoteDataSource, localDataSource)
        }

    @Test
    fun `updateAddress Should throw server failure when update address fails`() = runTest {
        whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
        whenever(remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel))
            .thenThrow(FailureException(tErrorMessage))
        val exception = assertFailsWith<Failures.ServerFailure> {
            repository.updateAddress(customerAddressParams = tAddressRequestEntity)

        }
        assert(exception.message == tErrorMessage)
        verify(localDataSource, never()).updateAddress(updateAddressParams = tAddressResponseModel)
    }

    @Test
    fun `getAddressById Should return address when internet connection is available`() = runTest {
        whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
        whenever(localDataSource.checkAddressEntityById(id = 1)).thenReturn(tCustomerAddressEntity)
        whenever(localDataSource.getAddressById(id = 1)).thenReturn(tCustomerAddressEntity)
        val result = repository.getAddressById(id = 1)
        assertEquals(tCustomerAddressEntity, result)
    }

    @Test
    fun `getAddressById should call remoteDataSource and update Address when localDataSource returns null`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
            whenever(localDataSource.checkAddressEntityById(id = 1)).thenReturn(null)
            whenever(remoteDataSource.getAddress()).thenReturn(tAddressResponseModel)
            whenever(localDataSource.updateAddress(updateAddressParams = tAddressResponseModel)).thenReturn(
                Unit
            )
            whenever(localDataSource.getAddressById(id = 1)).thenReturn(tCustomerAddressEntity)
            val result = repository.getAddressById(id = 1)
            assertEquals(tCustomerAddressEntity, result)
            verify(localDataSource).checkAddressEntityById(id = 1)
            verify(remoteDataSource).getAddress()
            verify(localDataSource).updateAddress(updateAddressParams = tAddressResponseModel)
            verify(localDataSource).getAddressById(id = 1)
        }

    @Test
    fun `getAddressById Should throw connection failure when internet connection is not available`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.getAddressById(id = 1)
            }
            assert(exception.message == "No Internet Connection")
            verifyNoInteractions(remoteDataSource, localDataSource)
        }

    @Test
    fun `getAddressById Should throw Cache failure when call localDataSource fails`() = runTest {
        whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
        whenever(localDataSource.checkAddressEntityById(id = 1))
            .thenThrow(FailureException(tErrorMessage))
        val exception = assertFailsWith<Failures.CacheFailure> {
            repository.getAddressById(id = 1)

        }
        assert(exception.message == tErrorMessage)
        verify(remoteDataSource, never()).getAddress()
        verify(localDataSource, never()).updateAddress(tAddressResponseModel)
        verify(localDataSource, never()).getAddressById(id = 1)
    }

    @Test
    fun `checkUpdateAddress should return address when internet connection is available and checkUpdate is true`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
            whenever(remoteDataSource.checkUpdateAddress()).thenReturn(
                tCheckUpdateAddressResponseModel
            )
            whenever(remoteDataSource.getAddress()).thenReturn(tAddressResponseModel)
            whenever(localDataSource.getAddressById(1)).thenReturn(tCustomerAddressEntity)
            val result = repository.checkUpdateAddress()
            assertEquals(tCustomerAddressEntity, result)
            verify(localDataSource).updateAddress(tAddressResponseModel)
        }

    @Test(expected = Failures.ServerFailure::class)
    fun `checkUpdateAddress should throw server failure when checkUpdate is return from takeIf null`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
            whenever(remoteDataSource.checkUpdateAddress()).thenReturn(
                tCheckUpdateAddressResponseModel
            )
            repository.checkUpdateAddress()
        }

    @Test
    fun `checkUpdateAddress should throw connection failure when internet connection is not available`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = assertFailsWith<Failures.ConnectionFailure> {
                repository.checkUpdateAddress()
            }
            assert(exception.message == "No Internet Connection")
            verifyNoInteractions(remoteDataSource, localDataSource)
        }

    @Test
    fun `checkUpdateAddress should throw cacheFailure when call localDataSource fails`() = runTest {
        whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
        whenever(remoteDataSource.checkUpdateAddress()).thenReturn(
            tCheckUpdateAddressResponseModel
        )
        whenever(localDataSource.getAddressById(1)).thenThrow(FailureException(tErrorMessage))
        val exception = assertFailsWith<Failures.CacheFailure> {
            repository.checkUpdateAddress()
        }
        assert(exception.message == tErrorMessage)

    }

    @Test
    fun `checkUpdateAddress should throw serverFailure when call remoteDataSource fails`() =
        runTest {
            whenever(internetConnectionChecker.hasConnection()).thenReturn(true)
            whenever(remoteDataSource.checkUpdateAddress()).thenThrow(FailureException(tErrorMessage))
            val exception = assertFailsWith<Failures.ServerFailure> {
                repository.checkUpdateAddress()
            }
            assert(exception.message == tErrorMessage)
        }

}

