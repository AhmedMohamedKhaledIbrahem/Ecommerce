package com.example.ecommerce.features.productsearch.data.datasources

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.features.productsearch.data.models.ProductSearchModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class ProductSearchRemoteDataSourceImplTest {
    @Mock
    private lateinit var productSearchApi: ProductSearchApi
    private lateinit var productSearchRemoteDataSourceImpl: ProductSearchRemoteDataSourceImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        productSearchRemoteDataSourceImpl =
            ProductSearchRemoteDataSourceImpl(productSearchApi = productSearchApi)
    }

    private val tCategoryId = "2478868012"
    private val tProductsSearchModel = listOf(
        ProductSearchModel(
            asin = "B0CRF7HBF8",
            climatePledgeFriendly = false,
            currency = "EGP",
            delivery = "احصل عليه الأربعاء، 16 أكتوبرتشحن من أمازون - شحن مجاني",
            hasVariations = false,
            isAmazonChoice = false,
            isBestSeller = false,
            isPrime = false,
            productAvailability = "In Stock",
            productMinimumOfferPrice = "13.75 جنيه",
            productNumOffers = 1,
            productNumRatings = 258,
            productOriginalPrice = null,
            productPhoto = "https://m.media-amazon.com/images/I/61TEazg2PxL._AC_UL960_FMwebp_QL65_.jpg",
            productPrice = "13.75 جنيه",
            productStarRating = "4.5",
            productTitle = "علبة كولا سوبر صودا من في 300 مل",
            productUrl = "https://www.amazon.eg/dp/B0CRF7HBF8",
            salesVolume = "تم شراء 1 ألف+ سلعة في الشهر الماضي\""
        )
    )

    @Test
    fun `getProductByCategory should return list of products when the response is successful`() =
        runTest {
            val response = Response.success(tProductsSearchModel)
            `when`(
                productSearchApi.getProductByCategory(
                    categoryId = tCategoryId,
                    page = 1
                )
            ).thenReturn(response)
            val result = productSearchRemoteDataSourceImpl.getProductByCategory(tCategoryId)
            assertEquals(tProductsSearchModel, result)
            verify(productSearchApi).getProductByCategory(categoryId = tCategoryId, page = 1)
        }

    @Test
    fun `getProductByCategory should throw Failure Exception when the response code is 400 or higher`() =
        runTest {
            val response = Response.error<List<ProductSearchModel>>(
                400,
                okhttp3.ResponseBody.create(null, "server Error")
            )
            `when`(
                productSearchApi.getProductByCategory(
                    categoryId = tCategoryId,
                    page = 1
                )
            ).thenReturn(response)
            val result = assertFailsWith<FailureException> {
                productSearchRemoteDataSourceImpl.getProductByCategory(tCategoryId)
            }
            assertEquals("Server Error: Response.error()", result.message)
            verify(productSearchApi).getProductByCategory(categoryId = tCategoryId, page = 1)
        }

    @Test
    fun `getProductByCategory should throw FailureException when an exception occurs`() = runTest {
        `when`(
            productSearchApi.getProductByCategory(
                tCategoryId,
                1
            )
        ).thenThrow(RuntimeException("Network Error"))

        val exception = assertFailsWith<FailureException> {
            productSearchRemoteDataSourceImpl.getProductByCategory(tCategoryId)
        }
        assertEquals("Network Error", exception.message)
        verify(productSearchApi).getProductByCategory(tCategoryId, 1)
    }
}
