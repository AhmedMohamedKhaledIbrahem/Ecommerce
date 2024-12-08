package com.example.ecommerce.features.productsearch.data.respositories

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import com.example.ecommerce.core.network.checknetwork.InternetConnectionCheckerImp
import com.example.ecommerce.features.productsearch.data.datasources.ProductSearchRemoteDataSource
import com.example.ecommerce.features.productsearch.data.mapper.ProductMapper
import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel
import com.example.ecommerce.features.productsearch.data.repositories.ProductSearchRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@ExperimentalCoroutinesApi
class ProductSearchRepositoryImplTest {
    @Mock
    private lateinit var internetConnectionChecker: InternetConnectionCheckerImp

    @Mock
    private lateinit var remoteDataSource: ProductSearchRemoteDataSource

    private lateinit var productSearchRepositoryImpl: ProductSearchRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        productSearchRepositoryImpl = ProductSearchRepositoryImpl(
            remoteDataSource = remoteDataSource,
            internetConnectionChecker = internetConnectionChecker
        )
    }

    private val tCategoryId = "1"
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
        )
    )

    @Test
    fun `getProductByCategory should return list of products when the internet is online`() =
        runTest {

            val tProductSearchEntity = ProductMapper.mapToEntityList(tProductSearchModel)
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            `when`(remoteDataSource.getProductByCategory(tCategoryId)).thenReturn(
                tProductSearchModel
            )
            val result = productSearchRepositoryImpl.getProductsByCategory(tCategoryId)
            assertEquals(tProductSearchEntity, result)
            verify(internetConnectionChecker).hasConnection()
            verify(remoteDataSource).getProductByCategory(tCategoryId)

        }

    @Test
    fun `getProductByCategory should  throw the connectionFailure when the internet is offline`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(false)
            val result = assertFailsWith<Failures.ConnectionFailure> {
                productSearchRepositoryImpl.getProductsByCategory(categoryId = tCategoryId)
            }
            assertEquals("No Internet Connection", result.message)
            verify(internetConnectionChecker).hasConnection()
            verifyNoMoreInteractions(remoteDataSource)
        }

    @Test
    fun `getProductByCategory should throw the serverFailure on remote Data source is failure`() =
        runTest {
            `when`(internetConnectionChecker.hasConnection()).thenReturn(true)
            doThrow(FailureException("Unknown Server error")).`when`(remoteDataSource).getProductByCategory(tCategoryId)
            val result = assertFailsWith<Failures.ServerFailure> {
                productSearchRepositoryImpl.getProductsByCategory(categoryId = tCategoryId)
            }
            assertEquals("Unknown Server error", result.message)
            verify(internetConnectionChecker).hasConnection()
            verify(remoteDataSource).getProductByCategory(categoryId = tCategoryId)
        }
}