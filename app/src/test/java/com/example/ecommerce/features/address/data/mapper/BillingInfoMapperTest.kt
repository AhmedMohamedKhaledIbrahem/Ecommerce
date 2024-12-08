package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.BillingInfoRequestModel
import com.example.ecommerce.features.address.data.models.BillingInfoResponseModel
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoResponseEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import org.junit.Test
import kotlin.test.assertEquals

class BillingInfoMapperTest {


    // private val billingInfoEntityJson = fixture(fixture("billingInfo.json")).run {
    //    Gson().fromJson(this, BillingInfoRequestEntity::class.java)
    // }

    private val tBillingInfoResponseEntity = BillingInfoResponseEntity(
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
    private val tBillingInfoRequestModel = BillingInfoRequestModel(
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

    @Test
    fun `should map BillingInfoResponseModel to the BillingInfoResponseEntity`() {
        val billingInfoModelResponseJson = fixture("billingInfo.json")
        val json =
            Gson().fromJson(billingInfoModelResponseJson, BillingInfoResponseModel::class.java)
        val result = BillingInfoMapper.mapToEntity(model = json)
        assertEquals(tBillingInfoResponseEntity, result)
    }

    @Test
    fun `should map BillingInfoRequestEntity to the BillingInfoRequestModel`() {
        val result = BillingInfoMapper.mapToModel(entity = tBillingInfoRequestEntity)
        assertEquals(tBillingInfoRequestModel, result)
    }


}