package com.example.ecommerce.core.database.data.mapper

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressRequestModel

object CustomerAddressMapper {
    fun mapToEntity(model: AddressRequestModel): CustomerAddressEntity {
        return CustomerAddressEntity(
            firstName = model.billing?.firstName,
            lastName = model.billing?.lastName,
            email = model.billing?.email,
            phone = model.billing?.phone,
            address = model.billing?.address,
            country = model.billing?.country,
            city = model.billing?.city,
            zipCode = model.billing?.postCode,
        )
    }

    fun mapToEntity(id: Int, model: AddressRequestModel): CustomerAddressEntity {
        return CustomerAddressEntity(
            id = id,
            firstName = model.billing?.firstName,
            lastName = model.billing?.lastName,
            email = model.billing?.email,
            phone = model.billing?.phone,
            address = model.billing?.address,
            country = model.billing?.country,
            city = model.billing?.city,
            zipCode = model.billing?.postCode,
        )
    }
}