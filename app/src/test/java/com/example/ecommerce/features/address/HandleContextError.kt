package com.example.ecommerce.features.address

import android.content.Context
import com.example.ecommerce.R
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.`when`

fun noAddressFound(context: Context, message: String) = runTest {
    `when`(context.getString(R.string.no_address_found)).thenReturn(message)
}

fun unKnownError(context: Context, message: String) = runTest {
    `when`(context.getString(R.string.unknown_error)).thenReturn(message)
}