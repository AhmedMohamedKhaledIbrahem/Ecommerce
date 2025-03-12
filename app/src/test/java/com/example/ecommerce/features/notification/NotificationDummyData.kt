package com.example.ecommerce.features.notification

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

const val tToken = "231@"
const val tOrderId = 1
const val tStatus = "completed"
val tMapToken = mapOf("token" to tToken)

const val tResponseFCMTokenJson = """{"status": "FCM token saved"}"""
val tResponseFCMTokenBody = tResponseFCMTokenJson.toResponseBody("application/json".toMediaType())
const val tErrorResponseFCMTokenJson = """{"status": "Failed to save FCM token"}"""
val tErrorResponseFCMTokenBody = tErrorResponseFCMTokenJson.toResponseBody("application/json".toMediaType())
const val tExceptionError = "Failed to save FCM token"