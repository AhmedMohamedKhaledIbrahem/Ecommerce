package com.example.ecommerce.features.address.data.mapper

import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.features.address.data.models.AddressDataResponseModel
import com.example.ecommerce.features.address.data.models.AddressRequestModel
import com.example.ecommerce.features.address.data.models.UpdateAddressResponseModel
import com.example.ecommerce.features.address.domain.entites.AddressRequestEntity
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.UpdateAddressResponseEntity

object AddressMapper {
    fun mapToEntity(model: UpdateAddressResponseModel): UpdateAddressResponseEntity {
        return UpdateAddressResponseEntity(
            message = model.message,
        )
    }

    fun mapToModel(entity: AddressRequestEntity): AddressRequestModel {
        return AddressRequestModel(
            billing = BillingInfoMapper.mapToModel(entity.billing) ,
            shipping =   ShippingInfoMapper.mapToModel(entity.shipping) ,
        )
    }

    fun mapAddressResponseModelToAddressRequestModel(model: AddressDataResponseModel): AddressRequestModel {

        return AddressRequestModel(
            billing = BillingInfoMapper.mapBillingInfoResponseModelToBillingInfoRequestModel(
                model.billing
            ),
        )

    }

    fun mapCustomerAddressEntityToBillingInfoRequest(addressEntity: CustomerAddressEntity): BillingInfoRequestEntity {
        return BillingInfoRequestEntity(
            firstName = addressEntity.firstName,
            lastName = addressEntity.lastName,
            address = addressEntity.address,
            city = addressEntity.city,
            country = addressEntity.country,
            postCode = addressEntity.zipCode,
            phone = addressEntity.phone,
            email = addressEntity.email,
        )
    }
}