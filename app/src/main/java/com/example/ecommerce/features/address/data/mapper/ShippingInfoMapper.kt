package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.ShippingInfoRequestModel
import com.example.ecommerce.features.address.data.models.ShippingInfoResponseModel
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoResponseEntity

object ShippingInfoMapper {
    fun mapToEntity(model:ShippingInfoResponseModel):ShippingInfoResponseEntity{
        return ShippingInfoResponseEntity(
            firstName = model.firstName,
            lastName = model.lastName,
            address = model.address,
            city = model.city,
            state = model.state,
            postCode = model.postCode,
            country = model.country,
        )
    }
    fun mapToModel(entity:ShippingInfoRequestEntity):ShippingInfoRequestModel{
        return ShippingInfoRequestModel(
            firstName = entity.firstName,
            lastName = entity.lastName,
            address = entity.address,
            city = entity.city,
            state = entity.state,
            postCode = entity.postCode,
            country = entity.country,
        )
    }
}