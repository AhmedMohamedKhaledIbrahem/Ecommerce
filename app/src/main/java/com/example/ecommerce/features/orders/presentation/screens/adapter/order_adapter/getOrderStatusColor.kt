package com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.ecommerce.R

fun getOrderStatusColor(context: Context, status: String): Int {
    return when (status.lowercase()) {
        "processing" -> ContextCompat.getColor(context, R.color.orange)
        "completed" -> ContextCompat.getColor(context, R.color.green)
        "pending" -> ContextCompat.getColor(context, R.color.blue)
        "on-hold" -> ContextCompat.getColor(context, R.color.yellow)
        "cancelled" -> ContextCompat.getColor(context, R.color.gray)
        "refunded" -> ContextCompat.getColor(context, R.color.purple)
        "failed" -> ContextCompat.getColor(context, R.color.red)
        else -> ContextCompat.getColor(context, android.R.color.darker_gray)
    }
}
