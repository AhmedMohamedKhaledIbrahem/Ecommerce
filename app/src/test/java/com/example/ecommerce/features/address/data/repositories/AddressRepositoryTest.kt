package com.example.ecommerce.features.address.data.repositories

import android.content.Context
import com.example.ecommerce.R
import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import com.example.ecommerce.features.address.data.datasources.localdatasource.AddressLocalDataSource
import com.example.ecommerce.features.address.data.datasources.remotedatasource.AddressRemoteDataSource
import com.example.ecommerce.features.address.id
import com.example.ecommerce.features.address.tAddressDataResponseModel
import com.example.ecommerce.features.address.tAddressRequestEntity
import com.example.ecommerce.features.address.tAddressRequestModel
import com.example.ecommerce.features.address.tCustomerAddressEntity
import com.example.ecommerce.features.address.tCustomerId
import com.example.ecommerce.features.address.tListCustomerAddressEntity
import com.example.ecommerce.features.address.tUpdateAddressResponseEntity
import com.example.ecommerce.features.address.tUpdateAddressResponseModel
import com.example.ecommerce.features.cacheFailure
import com.example.ecommerce.features.cacheFailureMessage
import com.example.ecommerce.features.connectionFailure
import com.example.ecommerce.features.serverFailure
import com.example.ecommerce.features.serverFailureMessage
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AddressRepositoryTest {
    @Mock
    private lateinit var remoteDataSource: AddressRemoteDataSource

    @Mock
    private lateinit var localDataSource: AddressLocalDataSource

    @Mock
    private lateinit var internetConnectionChecker: InternetConnectionChecker
    private var context: Context = mockk(relaxed = true)
    private lateinit var repository: AddressRepositoryImp


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        repository = AddressRepositoryImp(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            internetConnectionChecker = internetConnectionChecker,
            context = context
        )
    }

    private val expectedMessage = context.getString(R.string.no_internet_connection)

    @Test
    fun `updateAddress Should update address when internet connection is available`() = runTest {
        `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
        `when`(
            localDataSource.updateAddress(
                id,
                addressRequestParams = tAddressRequestModel
            )
        ).thenReturn(Unit)
        `when`(remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel))
            .thenReturn(tUpdateAddressResponseModel)
        val result = repository.updateAddress(id, customerAddressParams = tAddressRequestEntity)
        assertEquals(tUpdateAddressResponseEntity, result)
    }

    @Test
    fun `updateAddress Should throw connection failure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)

            val exception = connectionFailure {
                repository.updateAddress(id, customerAddressParams = tAddressRequestEntity)

            }
            assertEquals(expectedMessage, exception.message)

        }

    @Test
    fun `updateAddress Should throw cache failure when update address fails`() = runTest {
        `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
        `when`(
            localDataSource.updateAddress(
                id,
                addressRequestParams = tAddressRequestModel
            )
        ).thenThrow(
            FailureException(
                cacheFailureMessage
            )
        )
        `when`(remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
            tUpdateAddressResponseModel
        )

        val exception = cacheFailure {
            repository.updateAddress(id, customerAddressParams = tAddressRequestEntity)

        }
        assertEquals(cacheFailureMessage, exception.message)

    }

    @Test
    fun `updateAddress Should throw server failure when update address fails`() = runTest {
        `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
        `when`(remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel))
            .thenThrow(FailureException(serverFailureMessage))
        val exception = serverFailure {
            repository.updateAddress(id, customerAddressParams = tAddressRequestEntity)

        }
        assertEquals(serverFailureMessage, exception.message)

    }

    @Test
    fun `getAddress Should return list of address when  internet connection is available and the address is not empty`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.isAddressEmpty()).thenReturn(false)
            `when`(localDataSource.getAddress()).thenReturn(tListCustomerAddressEntity)
            val result = repository.getAddress()
            assertEquals(tListCustomerAddressEntity, result)
        }

    @Test
    fun `getAddress should call remoteDataSource and insert Address then return list of address when the address is  empty and internet connection is available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.isAddressEmpty()).thenReturn(true)
            `when`(remoteDataSource.getAddress()).thenReturn(tAddressDataResponseModel)
            `when`(localDataSource.insertAddress(addressRequestParams = tAddressRequestModel)).thenReturn(
                Unit
            )
            `when`(localDataSource.getAddress()).thenReturn(tListCustomerAddressEntity)
            val result = repository.getAddress()
            assertEquals(tListCustomerAddressEntity, result)

        }

    @Test
    fun `getAddress Should throw connection failure when internet connection is not available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = connectionFailure {
                repository.getAddress()
            }
            assertEquals(expectedMessage, exception.message)
        }

    @Test
    fun `getAddress Should throw Cache failure when address is not empty ,internet connection is available and call localDataSource throw exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.isAddressEmpty()).thenReturn(false)
            `when`(localDataSource.getAddress())
                .thenThrow(FailureException(cacheFailureMessage))
            val exception = cacheFailure {
                repository.getAddress()

            }
            assertEquals(cacheFailureMessage, exception.message)
        }

    @Test
    fun `getAddress Should throw Server failure when address is  empty ,internet connection is available and call localDataSource throw exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.isAddressEmpty()).thenReturn(true)
            `when`(remoteDataSource.getAddress()).thenAnswer {
                throw FailureException(
                    serverFailureMessage
                )
            }
            val exception = serverFailure {
                repository.getAddress()

            }
            assertEquals(serverFailureMessage, exception.message)
        }

    @Test
    fun `insertAddress should insert address when internet connection is available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.insertAddress(tAddressRequestModel)).thenReturn(Unit)
            `when`(remoteDataSource.updateAddress(tAddressRequestModel)).thenReturn(
                tUpdateAddressResponseModel
            )
            repository.insertAddress(tAddressRequestEntity)
            verify(internetConnectionChecker).hasConnection()
            verify(localDataSource).insertAddress(tAddressRequestModel)
            verify(remoteDataSource).updateAddress(tAddressRequestModel)

        }

    @Test
    fun `insertAddress should throw connection failure when internet connection isn't available`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val exception = connectionFailure {
                repository.insertAddress(tAddressRequestEntity)
            }
            assertEquals(expectedMessage, exception.message)
        }

    @Test
    fun `insertAddress should throw cache failure when localDataSource throw exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.insertAddress(tAddressRequestModel)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.insertAddress(tAddressRequestEntity)
            }
            assertEquals(cacheFailureMessage, exception.message)
        }

    @Test
    fun `insertAddress should throw server failure when remoteDataSource throw exception`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(localDataSource.insertAddress(tAddressRequestModel)).thenReturn(Unit)
            `when`(remoteDataSource.updateAddress(tAddressRequestModel)).thenThrow(
                FailureException(
                    serverFailureMessage
                )
            )
            val exception = serverFailure {
                repository.insertAddress(tAddressRequestEntity)
            }
            assertEquals(serverFailureMessage, exception.message)
        }

    @Test
    fun `deleteAllAddress should delete all address `() = runTest {
        `when`(localDataSource.deleteAllAddress()).thenReturn(Unit)
        repository.deleteAllAddress()
        verify(localDataSource).deleteAllAddress()
    }

    @Test
    fun `deleteAllAddress should throw cache failure when localDataSource throw exception `() =
        runTest {
            `when`(localDataSource.deleteAllAddress()).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.deleteAllAddress()
            }
            assertEquals(cacheFailureMessage, exception.message)

        }

    @Test
    fun `deleteAddress should delete  address `() = runTest {
        `when`(localDataSource.deleteAddress(tCustomerAddressEntity)).thenReturn(Unit)
        repository.deleteAddress(tCustomerAddressEntity)
        verify(localDataSource).deleteAddress(tCustomerAddressEntity)
    }

    @Test
    fun `deleteAddress should throw cache failure when localDataSource throw exception `() =
        runTest {
            `when`(localDataSource.deleteAddress(tCustomerAddressEntity)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.deleteAddress(tCustomerAddressEntity)
            }
            assertEquals(cacheFailureMessage, exception.message)

        }

    @Test
    fun `getSelectAddress should select address only the is selected equal 1 `() = runTest {
        `when`(localDataSource.getSelectAddress(tCustomerId)).thenReturn(tCustomerAddressEntity)
        val result = repository.getSelectAddress(tCustomerId)
        assertEquals(tCustomerAddressEntity, result)
    }

    @Test
    fun `getSelectAddress should throw cache failure when localDataSource throw exception `() =
        runTest {
            `when`(localDataSource.getSelectAddress(tCustomerId)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.getSelectAddress(tCustomerId)
            }
            assertEquals(cacheFailureMessage, exception.message)

        }

    @Test
    fun `selectAddress should select address `() = runTest {
        `when`(localDataSource.selectAddress(tCustomerId)).thenReturn(Unit)
        repository.selectAddress(tCustomerId)
        verify(localDataSource).selectAddress(tCustomerId)
    }

    @Test
    fun `selectAddress should throw cache failure when localDataSource throw exception `() =
        runTest {
            `when`(localDataSource.selectAddress(tCustomerId)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.selectAddress(tCustomerId)
            }
            assertEquals(cacheFailureMessage, exception.message)

        }
    @Test
    fun `unSelectAddress should unselect address unless the selected address `() = runTest {
        `when`(localDataSource.unSelectAddress(tCustomerId)).thenReturn(Unit)
        repository.unSelectAddress(tCustomerId)
        verify(localDataSource).unSelectAddress(tCustomerId)
    }

    @Test
    fun `unselectAddress should throw cache failure when localDataSource throw exception `() =
        runTest {
            `when`(localDataSource.unSelectAddress(tCustomerId)).thenThrow(
                FailureException(
                    cacheFailureMessage
                )
            )
            val exception = cacheFailure {
                repository.unSelectAddress(tCustomerId)
            }
            assertEquals(cacheFailureMessage, exception.message)

        }


}

