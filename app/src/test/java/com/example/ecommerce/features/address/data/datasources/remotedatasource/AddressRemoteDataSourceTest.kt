package com.example.ecommerce.features.address.data.datasources.remotedatasource

import android.content.Context
import com.example.ecommerce.features.address.data.datasources.AddressApi
import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.tAddressDataResponseModel
import com.example.ecommerce.features.address.tAddressRequestModel
import com.example.ecommerce.features.address.tErrorResponseAddressTokenBody
import com.example.ecommerce.features.address.tUpdateAddressResponseModel
import com.example.ecommerce.features.errorBody
import com.example.ecommerce.features.errorMessage
import com.example.ecommerce.features.errorResponseBody
import com.example.ecommerce.features.failureException
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AddressRemoteDataSourceTest {
    @Mock
    private lateinit var api: AddressApi
    private lateinit var context: Context
    private lateinit var remoteDataSource: AddressRemoteDataSourceImp


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = mockk(relaxed = true)
        remoteDataSource = AddressRemoteDataSourceImp(api = api, context)
    }



    @Test
    fun `updateAddress should return response when call the remote data source success`() =
        runTest {
            val response = Response.success(tUpdateAddressResponseModel)
            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
                response
            )
            val result = remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
            assertEquals(tUpdateAddressResponseModel, result)
        }

    @Test
    fun `updateAddress should throw Failure exception when the body of response return Empty`() =
        runTest {
            val response = Response.success<UpdateAddressResponseModel>(null)

            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
                response
            )
            val exception = failureException {
                remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `updateAddress throws FailureException when call remote data source have code 400 or higher`() =
        runTest {
            val errorResponse = Response.error<UpdateAddressResponseModel>(
                400,
                tErrorResponseAddressTokenBody
            )
            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
                errorResponse
            )

            val exception = failureException {
                remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `updateAddress should throw FailureException when API call throws exception`() =
        runTest {

            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenThrow(
                RuntimeException(errorMessage)
            )

            val exception = failureException {
                remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `getAddress should return response when call the remote data source success`() =
        runTest {
            val response = Response.success(tAddressDataResponseModel)
            whenever(api.getAddress()).thenReturn(
                response
            )
            val result = remoteDataSource.getAddress()
            assertEquals(tAddressDataResponseModel, result)
        }

    @Test
    fun `getAddress should throw Failure exception when the body of response return Empty`() =
        runTest {
            val response = Response.success<AddressDataResponseModel>(null)

            whenever(api.getAddress()).thenReturn(
                response
            )
            val exception = failureException {
                remoteDataSource.getAddress()
            }
            assertEquals(errorResponseBody, exception.message)
        }

    @Test
    fun `getAddress throws FailureException when call remote data source have code 400 or higher`() =
        runTest {
            val errorResponse = Response.error<AddressDataResponseModel>(
                400,
                errorBody
            )
            whenever(api.getAddress()).thenReturn(
                errorResponse
            )

            val exception = failureException {
                remoteDataSource.getAddress()
            }
            assertEquals(errorMessage, exception.message)
        }

    @Test
    fun `getAddress should throw FailureException when API call throws exception`() =
        runTest {

            whenever(api.getAddress()).thenThrow(
                RuntimeException(errorMessage)
            )

            val exception = failureException {
                remoteDataSource.getAddress()
            }
            assertEquals(errorMessage, exception.message)
        }



}