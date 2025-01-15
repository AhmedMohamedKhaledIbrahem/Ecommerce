package com.example.ecommerce.features.cart.data.data_soruce.local

import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity

fun calculateTotalPrice(items: List<ItemCartEntity>): Double{
    return items.sumOf { item ->
        val price = item.price.toDouble() // Convert price to Double
        val quantity = item.quantity.toDouble()
        price * quantity
    }
}