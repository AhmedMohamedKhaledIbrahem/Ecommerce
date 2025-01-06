package com.example.ecommerce.core.database.data.mapper

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressResponseModel

object CustomerAddressMapper {
    fun mapToEntity(model : AddressResponseModel): CustomerAddressEntity {
        return CustomerAddressEntity(
            id = 0 ,
            userId = model.userId,
            firstName = model.billing?.firstName,
            lastName = model.billing?.lastName,
            email = model.billing?.email,
            phone = model.billing?.phone,
            address = model.billing?.address,
            country = model.billing?.country,
            city = model.billing?.city,
            state = model.billing?.state,
            zipCode = model.billing?.postCode,
        )
    }
}