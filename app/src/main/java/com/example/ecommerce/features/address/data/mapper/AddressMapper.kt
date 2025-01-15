package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.AddressResponseModel
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.AddressResponseEntity

object AddressMapper {
    fun mapToEntity(model: AddressResponseModel): AddressResponseEntity {
        return AddressResponseEntity(
            userId = model.userId,
            message = model.message,
            billing = model.data?.billing?.let { BillingInfoMapper.mapToEntity(it) },
            shipping = model.data?.shipping?.let { ShippingInfoMapper.mapToEntity(it) },
        )
    }
    fun mapToModel(entity:AddressRequestEntity):AddressRequestModel{
        return AddressRequestModel(
            billing = entity.billing?.let { BillingInfoMapper.mapToModel(it) },
            shipping = entity.shipping?.let { ShippingInfoMapper.mapToModel(it) },
        )
    }
}