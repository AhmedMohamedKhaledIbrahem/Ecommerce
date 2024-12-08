package com.example.ecommerce.features.productsearch.data.mapper

import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel
import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity
import org.junit.Test
import kotlin.test.assertEquals

class ProductMapperTest {
    private val tProductSearchModel = listOf(
        ProductSearchModel(
            asin = "1",
            climatePledgeFriendly = true,
            currency = "USD",
            delivery = "Free",
            hasVariations = false,
            isAmazonChoice = true,
            isBestSeller = false,
            isPrime = true,
            productAvailability = "In Stock",
            productMinimumOfferPrice = "10.00",
            productNumOffers = 5,
            productNumRatings = 100,
            productOriginalPrice = "15.00",
            productPhoto = "http://example.com/photo1.jpg",
            productPrice = "12.00",
            productStarRating = "4.5",
            productTitle = "Product 1",
            productUrl = "http://example.com/product1",
            salesVolume = "High"
        ),
        ProductSearchModel(
            asin = "2",
            climatePledgeFriendly = true,
            currency = "USD",
            delivery = "Free",
            hasVariations = false,
            isAmazonChoice = true,
            isBestSeller = false,
            isPrime = true,
            productAvailability = "In Stock",
            productMinimumOfferPrice = "10.00",
            productNumOffers = 5,
            productNumRatings = 100,
            productOriginalPrice = "15.00",
            productPhoto = "http://example.com/photo1.jpg",
            productPrice = "12.00",
            productStarRating = "4.5",
            productTitle = "Product 1",
            productUrl = "http://example.com/product1",
            salesVolume = "High"
        ),
    )
    @Test
    fun`MapToEntityList should correctly convert list of ProductSearchModel to ProductSearchEntity`(){
        val expectedProductSearchEntity = listOf(
            ProductSearchEntity(
                asin = "1",
                climatePledgeFriendly = true,
                currency = "USD",
                delivery = "Free",
                hasVariations = false,
                isAmazonChoice = true,
                isBestSeller = false,
                isPrime = true,
                productAvailability = "In Stock",
                productMinimumOfferPrice = "10.00",
                productNumOffers = 5,
                productNumRatings = 100,
                productOriginalPrice = "15.00",
                productPhoto = "http://example.com/photo1.jpg",
                productPrice = "12.00",
                productStarRating = "4.5",
                productTitle = "Product 1",
                productUrl = "http://example.com/product1",
                salesVolume = "High"
            ),
            ProductSearchEntity(
                asin = "2",
                climatePledgeFriendly = true,
                currency = "USD",
                delivery = "Free",
                hasVariations = false,
                isAmazonChoice = true,
                isBestSeller = false,
                isPrime = true,
                productAvailability = "In Stock",
                productMinimumOfferPrice = "10.00",
                productNumOffers = 5,
                productNumRatings = 100,
                productOriginalPrice = "15.00",
                productPhoto = "http://example.com/photo1.jpg",
                productPrice = "12.00",
                productStarRating = "4.5",
                productTitle = "Product 1",
                productUrl = "http://example.com/product1",
                salesVolume = "High"
            ),
        )
    val tProductSearchEntity = ProductMapper.mapToEntityList(tProductSearchModel)
       assertEquals(expectedProductSearchEntity,tProductSearchEntity)
    }

}