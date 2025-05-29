package com.example.ecommerce.features.address.data.datasources.localdatasource

import android.content.Context
import com.example.ecommerce.core.database.data.dao.address.AddressDao
import com.example.ecommerce.features.address.addressError
import com.example.ecommerce.features.address.id
import com.example.ecommerce.features.address.tAddressRequestModel
import com.example.ecommerce.features.address.tCustomerAddressEntity
import com.example.ecommerce.features.address.tCustomerAddressEntityUpdate
import com.example.ecommerce.features.address.tCustomerId
import com.example.ecommerce.features.address.tListCustomerAddressEntity
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.failureException
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AddressLocalDataSourceTest {

    @Mock
    private lateinit var addressDao: AddressDao
    private lateinit var context: Context
    private lateinit var localDataSource: AddressLocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        context = mockk(relaxed = true)
        localDataSource = AddressLocalDataSourceImp(dao = addressDao, context = context)

    }

    private fun isAddressEmpty(isEmpty: Int) = runTest {
        `when`(addressDao.getCount()).thenReturn(isEmpty)
    }


    @Test
    fun `UpdateAddress should update the address in the database when the customerAddressEntity exist`() =
        runTest {
            isAddressEmpty(1)

            `when`(addressDao.updateAddress(tCustomerAddressEntityUpdate)).thenReturn(Unit)

            localDataSource.updateAddress(id, tAddressRequestModel)

            verify(addressDao).updateAddress(tCustomerAddressEntityUpdate)

        }

    @Test
    fun `updateAddress should throw a FailureException when getCount equal zero`() =
        runTest {
            `when`(addressDao.getCount()).thenThrow(RuntimeException(addressError))
            val exception = failureException {
                localDataSource.updateAddress(id, tAddressRequestModel)
            }
            assertEquals(addressError, exception.message)

        }

    @Test
    fun `updateAddress should throw a FailureException when unexpected error occurs`() = runTest {
        isAddressEmpty(1)
        `when`(addressDao.updateAddress(customerAddressEntity = tCustomerAddressEntityUpdate)).thenThrow(
            RuntimeException(errorMessage)
        )
        val exception = failureException {
            localDataSource.updateAddress(id, tAddressRequestModel)
        }
        assertEquals(errorMessage, exception.message)
    }

    @Test
    fun `getAddress should return the list of address when it exists`() = runTest {
        `when`(addressDao.getAddress()).thenReturn(listOf(tCustomerAddressEntityUpdate))
        val result = localDataSource.getAddress()
        assertEquals(tListCustomerAddressEntity, result)

    }

    @Test
    fun `getAddress should throw a FailureException when unexpected error occurs`() = runTest {
        whenever(addressDao.getAddress()).thenThrow(RuntimeException(errorMessage))
        val exception = failureException {
            localDataSource.getAddress()
        }
        assertEquals(errorMessage, exception.message)
    }

    @Test
    fun `insertAddress should insert the address in the database`() = runTest {
        `when`(addressDao.insertAddress(tCustomerAddressEntity)).thenReturn(Unit)
        localDataSource.insertAddress(tAddressRequestModel)
        verify(addressDao).insertAddress(eq(tCustomerAddressEntity))

    }

    @Test
    fun `insertAddress should throw a FailureException when unexpected error occurs`() =
        runTest {
            `when`(addressDao.insertAddress(tCustomerAddressEntity)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val exception = failureException {
                localDataSource.insertAddress(tAddressRequestModel)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `deleteAllAddress should delete the All address `() = runTest {
        `when`(addressDao.deleteAllAddress()).thenReturn(Unit)
        localDataSource.deleteAllAddress()
        verify(addressDao).deleteAllAddress()

    }

    @Test
    fun `deleteAllAddress should throw a FailureException when unexpected error occurs`() =
        runTest {
            `when`(addressDao.deleteAllAddress()).thenThrow(RuntimeException(errorMessage))
            val exception = failureException {
                localDataSource.deleteAllAddress()
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `deleteAddress should delete the All address `() = runTest {
        `when`(addressDao.deleteAddress(tCustomerAddressEntity)).thenReturn(Unit)
        localDataSource.deleteAddress(tCustomerAddressEntity)
        verify(addressDao).deleteAddress(tCustomerAddressEntity)

    }

    @Test
    fun `deleteAddress should throw a FailureException when unexpected error occurs`() =
        runTest {
            `when`(addressDao.deleteAddress(tCustomerAddressEntity)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val exception = failureException {
                localDataSource.deleteAddress(tCustomerAddressEntity)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `getSelectAddress should return the select address `() = runTest {
        `when`(addressDao.getSelectAddress(tCustomerId)).thenReturn(tCustomerAddressEntity)
        val result = localDataSource.getSelectAddress(tCustomerId)
        assertEquals(tCustomerAddressEntity, result)
    }

    @Test
    fun `getSelectAddress should throw a FailureException when unexpected error occurs `() =
        runTest {
            `when`(addressDao.getSelectAddress(tCustomerId)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val exception = failureException {
                localDataSource.getSelectAddress(tCustomerId)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `selectAddress should select the address `() = runTest {
        `when`(addressDao.selectAddress(tCustomerId)).thenReturn(Unit)
        localDataSource.selectAddress(tCustomerId)
        verify(addressDao).selectAddress(tCustomerId)
    }
    @Test
    fun `selectAddress should throw a FailureException when unexpected error occurs `() =
        runTest {
            `when`(addressDao.selectAddress(tCustomerId)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val exception = failureException {
                localDataSource.selectAddress(tCustomerId)
            }
            assertEquals(errorMessage, exception.message)
        }
    @Test
    fun `unSelectAddress should unselect all address unless the selected address `() = runTest {
        `when`(addressDao.unSelectAddress(tCustomerId)).thenReturn(Unit)
        localDataSource.unSelectAddress(tCustomerId)
        verify(addressDao).unSelectAddress(tCustomerId)
    }
    @Test
    fun `unselectAddress should throw a FailureException when unexpected error occurs `() =
        runTest {
            `when`(addressDao.unSelectAddress(tCustomerId)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )
            val exception = failureException {
                localDataSource.unSelectAddress(tCustomerId)
            }
            assertEquals(errorMessage, exception.message)
        }
}