package com.example.ecommerce.features.cart.presentation.screens.adapter

data class ItemCart(
    val name :String ,
    val price:Double,
    val imageItem:String,
    val quantity:Int = 1,
)
