package com.example.ecommerce.features.address.data.datasources.remotedatasource
//
//import com.example.ecommerce.core.errors.FailureException
//import com.example.ecommerce.features.address.data.datasources.AddressApi
//import com.example.ecommerce.features.address.data.models.AddressRequestModel
//import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
//import com.example.ecommerce.features.address.data.models.BillingInfoRequestModel
//import com.example.ecommerce.features.address.data.models.CheckUpdateAddressResponseModel
//import com.example.ecommerce.features.address.data.models.ShippingInfoRequestModel
//import com.example.ecommerce.resources.fixture
//import com.google.gson.Gson
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import okhttp3.ResponseBody.Companion.toResponseBody
//import org.junit.Before
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.MockitoAnnotations
//import org.mockito.kotlin.whenever
//import retrofit2.Response
//import kotlin.test.assertEquals
//import kotlin.test.assertFailsWith
//
//@ExperimentalCoroutinesApi
//class AddressRemoteDataSourceTest {
//    @Mock
//    private lateinit var api: AddressApi
//    private lateinit var remoteDataSource: AddressRemoteDataSourceImp
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.openMocks(this)
//        remoteDataSource = AddressRemoteDataSourceImp(api = api)
//    }
//
//    private val tUpdateAddressResponseModel = fixture("address.json").run {
//        Gson().fromJson(this, UpdateAddressResponseModel::class.java)
//    }
//    private val tCheckUpdateAddressResponseModel = fixture(" checkUpdateAddress.json").run {
//        Gson().fromJson(this, CheckUpdateAddressResponseModel::class.java)
//    }
//    private val tShippingInfoRequestModel = ShippingInfoRequestModel(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//    )
//    private val tBillingInfoRequestModel = BillingInfoRequestModel(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//        email = "billing@example.com",
//        phone = "555-555-5555"
//    )
//    private val tAddressRequestModel = AddressRequestModel(
//        billing = tBillingInfoRequestModel,
//        shipping = tShippingInfoRequestModel
//    )
//    private val errorBody = "{'message': 'error message'}".toResponseBody(null)
//    private val errorResponseBody = "Empty Response Body"
//    private val errorMessage = "error message"
//
//    @Test
//    fun `updateAddress should return response when call the remote data source success`() =
//        runTest {
//            val response = Response.success(tUpdateAddressResponseModel)
//            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
//                response
//            )
//            val result = remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
//            assertEquals(tUpdateAddressResponseModel, result)
//        }
//
//    @Test
//    fun `updateAddress should throw Failure exception when the body of response return Empty`() =
//        runTest {
//            val response = Response.success<UpdateAddressResponseModel>(null)
//
//            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
//                response
//            )
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
//            }
//            assertEquals(errorResponseBody, exception.message)
//        }
//
//    @Test
//    fun `updateAddress throws FailureException when call remote data source have code 400 or higher`() =
//        runTest {
//            val errorResponse = Response.error<UpdateAddressResponseModel>(
//                400,
//                errorBody
//            )
//            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenReturn(
//                errorResponse
//            )
//
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
//            }
//            assertEquals(errorMessage, exception.message)
//        }
//
//    @Test
//    fun `updateAddress should throw FailureException when API call throws exception`() =
//        runTest {
//
//            whenever(api.updateAddress(updateAddressParams = tAddressRequestModel)).thenThrow(
//                RuntimeException(errorMessage)
//            )
//
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.updateAddress(updateAddressParams = tAddressRequestModel)
//            }
//            assertEquals(errorMessage, exception.message)
//        }
//
//    @Test
//    fun `getAddress should return response when call the remote data source success`() =
//        runTest {
//            val response = Response.success(tUpdateAddressResponseModel)
//            whenever(api.getAddress()).thenReturn(
//                response
//            )
//            val result = remoteDataSource.getAddress()
//            assertEquals(tUpdateAddressResponseModel, result)
//        }
//
//    @Test
//    fun `getAddress should throw Failure exception when the body of response return Empty`() =
//        runTest {
//            val response = Response.success<UpdateAddressResponseModel>(null)
//
//            whenever(api.getAddress()).thenReturn(
//                response
//            )
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.getAddress()
//            }
//            assertEquals(errorResponseBody, exception.message)
//        }
//
//    @Test
//    fun `getAddress throws FailureException when call remote data source have code 400 or higher`() =
//        runTest {
//            val errorResponse = Response.error<UpdateAddressResponseModel>(
//                400,
//                errorBody
//            )
//            whenever(api.getAddress()).thenReturn(
//                errorResponse
//            )
//
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.getAddress()
//            }
//            assertEquals(errorMessage, exception.message)
//        }
//
//    @Test
//    fun `getAddress should throw FailureException when API call throws exception`() =
//        runTest {
//
//            whenever(api.getAddress()).thenThrow(
//                RuntimeException(errorMessage)
//            )
//
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.getAddress()
//            }
//            assertEquals(errorMessage, exception.message)
//        }
//
//    @Test
//    fun `checkUpdateAddress should return response when call the remote data source success`() =
//        runTest {
//            val response = Response.success(tCheckUpdateAddressResponseModel)
//            whenever(api.checkUpdateAddress()).thenReturn(
//                response
//            )
//            val result = remoteDataSource.checkUpdateAddress()
//            assertEquals(tCheckUpdateAddressResponseModel, result)
//        }
//
//    @Test
//    fun `checkUpdateAddress should throw Failure exception when the body of response return Empty`() =
//        runTest {
//            val response = Response.success<CheckUpdateAddressResponseModel>(null)
//
//            whenever(api.checkUpdateAddress()).thenReturn(
//                response
//            )
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.checkUpdateAddress()
//            }
//            assertEquals(errorResponseBody, exception.message)
//        }
//
//    @Test
//    fun `checkUpdateAddress throws FailureException when call remote data source have code 400 or higher`() =
//        runTest {
//            val errorResponse = Response.error<CheckUpdateAddressResponseModel>(
//                400,
//                errorBody
//            )
//            whenever(api.checkUpdateAddress()).thenReturn(
//                errorResponse
//            )
//
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.checkUpdateAddress()
//            }
//            assertEquals(errorMessage, exception.message)
//        }
//
//    @Test
//    fun `checkUpdateAddress should throw FailureException when API call throws exception`() =
//        runTest {
//
//            whenever(api.checkUpdateAddress()).thenThrow(
//                RuntimeException(errorMessage)
//            )
//
//            val exception = assertFailsWith<FailureException> {
//                remoteDataSource.checkUpdateAddress()
//            }
//            assertEquals(errorMessage, exception.message)
//        }
//
//}