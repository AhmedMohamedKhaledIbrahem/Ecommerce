package com.example.ecommerce.core.errors

sealed class Failures : Throwable() {
    data class ServerFailure(
        override val message: String = "ServerFailure",
         val resourceId: Int? = null
    ) : Failures()

    data class ConnectionFailure(
        override val message: String = "ConnectionFailure",
        val resourceId: Int? = null
    ) : Failures()

    data class CacheFailure(
        override val message: String = "CacheFailure",
        val resourceId: Int? = null
    ) : Failures()
}