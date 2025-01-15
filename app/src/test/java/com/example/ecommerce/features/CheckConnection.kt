package com.example.ecommerce.features

import com.example.ecommerce.core.network.checknetwork.InternetConnectionChecker
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.`when`

 fun checkInternet(
    connectionChecker: Boolean,
    internetConnectionChecker: InternetConnectionChecker
) = runTest {
    `when`(internetConnectionChecker.hasConnection()).thenReturn(connectionChecker)
}