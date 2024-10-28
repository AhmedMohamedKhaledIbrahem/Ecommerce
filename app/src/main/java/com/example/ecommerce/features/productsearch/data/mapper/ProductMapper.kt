package com.example.ecommerce.features.productsearch.data.mapper

import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel
import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity

object ProductMapper {
    private fun mapToEntity(model:ProductSearchModel):ProductSearchEntity{
        return  ProductSearchEntity(
            asin = model.asin,
            climatePledgeFriendly = model.climatePledgeFriendly,
            currency = model.currency,
            delivery = model.delivery,
            hasVariations = model.hasVariations,
            isAmazonChoice = model.isAmazonChoice,
            isBestSeller = model.isBestSeller,
            isPrime = model.isPrime,
            productAvailability = model.productAvailability,
            productMinimumOfferPrice = model.productMinimumOfferPrice,
            productNumOffers = model.productNumOffers,
            productNumRatings = model.productNumRatings,
            productOriginalPrice = model.productOriginalPrice,
            productPhoto = model.productPhoto,
            productPrice = model.productPrice,
            productStarRating = model.productStarRating,
            productTitle = model.productTitle,
            productUrl = model.productUrl,
            salesVolume = model.salesVolume,
        )
    }
    fun mapToEntityList(models: List<ProductSearchModel>): List<ProductSearchEntity> {
        return models.map { mapToEntity(it) }
    }
}