package com.example.ecommerce.core.database.data.mapper

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressRequestModel

object CustomerAddressMapper {
    fun mapToEntity(model: AddressRequestModel): CustomerAddressEntity {
        return CustomerAddressEntity(
            firstName = model.billing?.firstName.toString(),
            lastName = model.billing?.lastName.toString(),
            email = model.billing?.email.toString(),
            phone = model.billing?.phone.toString(),
            address = model.billing?.address.toString(),
            country = model.billing?.country.toString(),
            city = model.billing?.city.toString(),
            zipCode = model.billing?.postCode.toString(),
        )
    }

    fun mapToEntity(id: Int, model: AddressRequestModel): CustomerAddressEntity {
        return CustomerAddressEntity(
            id = id,
            firstName = model.billing?.firstName.toString(),
            lastName = model.billing?.lastName.toString(),
            email = model.billing?.email.toString(),
            phone = model.billing?.phone.toString(),
            address = model.billing?.address.toString(),
            country = model.billing?.country.toString(),
            city = model.billing?.city.toString(),
            zipCode = model.billing?.postCode.toString(),
        )
    }
}