package com.example.ecommerce.features.productsearch.domain.usecases

import com.example.ecommerce.features.productsearch.domain.entites.ProductSearchEntity
import com.example.ecommerce.features.productsearch.domain.repositories.ProductSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetProductsByCategoryTest {
    @Mock
    private  lateinit var productSearchRepository: ProductSearchRepository


    private  lateinit var getProductsByCategory: GetProductsByCategory

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        getProductsByCategory = GetProductsByCategory(productSearchRepository = productSearchRepository)

    }

    @Test
    fun `invoke should return list of products from the repository`(): Unit = runTest {
        val tCategoryId = "1"
        val tProducts = listOf(
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
        )
       `when`(productSearchRepository.getProductsByCategory(tCategoryId)).thenReturn(tProducts)
        val result = getProductsByCategory(tCategoryId)
        assertEquals(result , tProducts)
        verify(productSearchRepository).getProductsByCategory(tCategoryId)
        verifyNoMoreInteractions(productSearchRepository)
    }
}