package com.example.ecommerce.features.product.presentation.screen.product_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductDetails(
    val productId:String,
    val productName: String,
    val productPrice: String,
    val productDescription: String,
    val category: String,
    val image: String,
    val stock: String,
    val rating: Double

) : Parcelable
