package com.example.ecommerce.core.errors

sealed class Failures : Throwable() {
    data class ServerFailure(override val message: String = "ServerFailure") : Failures()
    data class ConnectionFailure(override val message: String = "ConnectionFailure") : Failures()
    data class CacheFailure(override val message: String = "CacheFailure") : Failures()
}