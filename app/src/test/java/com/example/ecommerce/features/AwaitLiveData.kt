package com.example.ecommerce.features

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.fail

fun await(latch: CountDownLatch) {
    try {
        latch.await(2, TimeUnit.SECONDS)
    } catch (e: InterruptedException) {
        fail("Timed out waiting for LiveData value")
    }
}