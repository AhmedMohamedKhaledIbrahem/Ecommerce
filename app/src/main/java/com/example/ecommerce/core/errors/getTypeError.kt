package com.example.ecommerce.core.errors

fun mapFailureMessage(failures: Failures):String{
    return  when(failures){
        is Failures.ServerFailure -> failures.message
        is Failures.ConnectionFailure -> failures.message
        is Failures.CacheFailure -> failures.message
    }
}