package com.example.ecommerce.features

import okhttp3.ResponseBody.Companion.toResponseBody

val errorBody = "{'message': 'error message'}".toResponseBody(null)
const val errorResponseBody = "Empty Response Body"
const val errorMessage = "error message"