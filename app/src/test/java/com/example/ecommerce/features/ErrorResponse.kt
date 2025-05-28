package com.example.ecommerce.features

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

val errorBody = """{'message': 'error message'}""".toResponseBody(null)
const val errorResponseBody = "Empty Response Body"
const val errorMessage = "error message"
const val errorJson = """{"message": "error message"}"""
val errorJsonBody = errorJson.toResponseBody("application/json".toMediaType())
