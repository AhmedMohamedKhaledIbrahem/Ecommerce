package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity

object AddressMapper {
    fun mapToEntity(model: UpdateAddressResponseModel): UpdateAddressResponseEntity {
        return UpdateAddressResponseEntity(
            message = model.message,
        )
    }

    fun mapToModel(entity: AddressRequestEntity): AddressRequestModel {
        return AddressRequestModel(
            billing = entity.billing?.let { BillingInfoMapper.mapToModel(it) },
            shipping = entity.shipping?.let { ShippingInfoMapper.mapToModel(it) },
        )
    }

    fun mapAddressResponseModelToAddressRequestModel(model: AddressDataResponseModel): AddressRequestModel {

        return AddressRequestModel(
            billing = model.billing?.let {
                BillingInfoMapper.mapBillingInfoResponseModelToBillingInfoRequestModel(
                    it
                )
            },
        )

    }
}