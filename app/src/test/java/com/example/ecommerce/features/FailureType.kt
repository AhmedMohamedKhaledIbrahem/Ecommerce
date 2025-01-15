package com.example.ecommerce.features

import com.example.ecommerce.core.errors.FailureException
import com.example.ecommerce.core.errors.Failures
import kotlinx.coroutines.runBlocking
import kotlin.test.assertFailsWith

 fun connectionFailure(
    onError: suspend () -> Unit
): Failures.ConnectionFailure = runBlocking {
    assertFailsWith<Failures.ConnectionFailure> {
        onError()
    }
}

 fun serverFailure(
    onError: suspend () -> Unit
): Failures.ServerFailure = runBlocking {
    assertFailsWith<Failures.ServerFailure> {
        onError()
    }
}

 fun cacheFailure(
    onError: suspend () -> Unit
): Failures.CacheFailure = runBlocking {
    assertFailsWith<Failures.CacheFailure> {
        onError()
    }
}

fun failureException(
    onError: suspend () -> Unit
): FailureException = runBlocking {
    assertFailsWith<FailureException> {
        onError()
    }
}

const val connectionFailureMessage = "No Internet Connection"
const val serverFailureMessage = "Server error"
const val cacheFailureMessage = "Cache error"