package com.example.ecommerce.core.database.data.mapper

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressResponseModel

object CustomerAddressMapper {
    fun mapToEntity(model : AddressResponseModel): CustomerAddressEntity {
        return CustomerAddressEntity(
            id = 0 ,
            userId = model.userId,
            firstName = model.data?.billing?.firstName,
            lastName = model.data?.billing?.lastName,
            email = model.data?.billing?.email,
            phone = model.data?.billing?.phone,
            address = model.data?.billing?.address,
            country = model.data?.billing?.country,
            city = model.data?.billing?.city,
            state = model.data?.billing?.state,
            zipCode = model.data?.billing?.postCode,
        )
    }
}