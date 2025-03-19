package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.data.models.BillingInfoRequestModel
import com.example.ecommerce.features.address.data.models.ShippingInfoRequestModel
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoResponseEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

//class AddressMapperTest {

//    private val tShippingInfoRequestEntity = ShippingInfoRequestEntity(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//    )
//    private val tBillingInfoRequestEntity = BillingInfoRequestEntity(
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
//    private val tAddressRequestEntity = AddressRequestEntity(
//        billing = tBillingInfoRequestEntity,
//        shipping = tShippingInfoRequestEntity
//    )
//
//    private val tShippingInfoRequestModel= ShippingInfoRequestModel(
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
//
//
//
//    private val tShippingInfoResponseEntity = ShippingInfoResponseEntity(
//        firstName = "John",
//        lastName = "Doe",
//        address = "123 Main St",
//        city = "Springfield",
//        state = "IL",
//        postCode = "62701",
//        country = "US",
//    )
//    private val tBillingInfoResponseEntity = BillingInfoResponseEntity(
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
//    private val tUpdateAddressResponseEntity = UpdateAddressResponseEntity(
//        userId = 1,
//        message = "Address updated successfully.",
//        billing = tBillingInfoResponseEntity,
//        shipping = tShippingInfoResponseEntity)
//
//
//    @Test
//    fun `should map AddressResponseModel to AddressResponseEntity`() = runTest {
//
//        val tUpdateAddressResponseModel = fixture("address.json").run {
//            Gson().fromJson(this, UpdateAddressResponseModel::class.java)
//        }
//        val result = AddressMapper.mapToEntity(model = tUpdateAddressResponseModel)
//        assertEquals(tUpdateAddressResponseEntity, result)
//    }
//
//    @Test
//    fun `should map AddressRequestEntity to AddressRequestModel`() = runTest {
//        val result = AddressMapper.mapToModel(entity = tAddressRequestEntity)
//        assertEquals(tAddressRequestModel, result)
//
//    }
//}