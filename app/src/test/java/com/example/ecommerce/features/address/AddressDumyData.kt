package com.example.ecommerce.features.address

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.mapper.CustomerAddressMapper
import com.example.ecommerce.features.address.data.mapper.AddressMapper
import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.resources.fixture
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

val tUpdateAddressResponseModel: UpdateAddressResponseModel = fixture("addressMessage.json").run {
    Gson().fromJson(this, UpdateAddressResponseModel::class.java)
}
val tAddressDataResponseModel: AddressDataResponseModel = fixture("address.json").run {
    Gson().fromJson(this, AddressDataResponseModel::class.java)
}
const val id = 1
val tListCustomerAddressEntity = listOf(
    CustomerAddressEntity(
        id = id,
        firstName = "John",
        lastName = "Doe",
        address = "123 Main St",
        city = "Springfield",
        zipCode = "62701",
        country = "US",
        email = "billing@example.com",
        phone = "555-555-5555"
    )
)
val billingInfoRequestEntity = BillingInfoRequestEntity(
    firstName = "John",
    lastName = "Doe",
    address = "123 Main St",
    city = "Springfield",
    postCode = "62701",
    country = "US",
    email = "billing@example.com",
    phone = "555-555-5555"
)

val shippingInfoRequestEntity = ShippingInfoRequestEntity(
    firstName = "John",
    lastName = "Doe",
    address = "123 Main St",
    city = "Springfield",
    postCode = "62701",
)
val tUpdateAddressResponseEntity = AddressMapper.mapToEntity(tUpdateAddressResponseModel)
val tAddressRequestEntity = AddressRequestEntity(
    billing = billingInfoRequestEntity,
    shipping = shippingInfoRequestEntity
)


val tAddressRequestModel = AddressMapper.mapToModel(tAddressRequestEntity)
val tCustomerAddressEntity = CustomerAddressMapper.mapToEntity(
    model = tAddressRequestModel
)
val tCustomerAddressEntityUpdate = CustomerAddressMapper.mapToEntity(
    id = id,
    model = tAddressRequestModel
)
const val addressError = "No address found"
const val unknownError = "Unknown error"
const val tCustomerId = 1
const val tErrorResponseAddressJson = """{"message": "error message"}"""
val tErrorResponseAddressTokenBody = tErrorResponseAddressJson.toResponseBody("application/json".toMediaType())
