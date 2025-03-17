package com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.ecommerce.R

fun getOrderStatusColor(context: Context, status: String): Int {
    return when (status.lowercase()) {
        "processing" -> ContextCompat.getColor(context, R.color.order_processing)
        "completed" -> ContextCompat.getColor(context, R.color.order_completed)
        "pending" -> ContextCompat.getColor(context, R.color.order_pending)
        "on-hold" -> ContextCompat.getColor(context, R.color.order_on_hold)
        "cancelled" -> ContextCompat.getColor(context, R.color.order_cancelled)
        "refunded" -> ContextCompat.getColor(context, R.color.order_refunded)
        "failed" -> ContextCompat.getColor(context, R.color.order_failed)
        else -> ContextCompat.getColor(context, android.R.color.darker_gray)
    }
}
