package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity

class CartViewHolder(
    view: View,
    private val onCounterUpdate: (ItemCartEntity, Int) -> Unit,

    ) : RecyclerView.ViewHolder(view) {
    private val cartItemImage: ImageView = itemView.findViewById(R.id.cartItemImage)
    private val cartItemName: TextView = itemView.findViewById(R.id.nameItemCartText)
    private val cartItemPrice: TextView = itemView.findViewById(R.id.priceItemCartText)
    private val cartItemQuantity: TextView = itemView.findViewById(R.id.cartItemPriceTextView)
    private val cartItemIncrease: ImageView = itemView.findViewById(R.id.itemCartIncreaseCounter)
    private val cartItemDecrease: ImageView = itemView.findViewById(R.id.itemCartDecreaseCounter)


    @SuppressLint("SetTextI18n")
    fun bind(item: ItemCartEntity) {
        cartItemName.text = item.name
        cartItemPrice.text = item.price
        cartItemQuantity.text = item.quantity.toString()
        cartItemIncrease.setOnClickListener {
            if (item.quantity < 10) {

                val newQuantity = item.quantity + 1
                item.quantity = newQuantity // Update the item quantity
                cartItemQuantity.text = newQuantity.toString() // Update the UI
                onCounterUpdate(item, newQuantity) // Notify the callback

            } else {
                cartItemIncrease.isEnabled = false
            }

        }
        cartItemDecrease.setOnClickListener {
            if (item.quantity > 1) {
                val newQuantity = item.quantity - 1
                item.quantity = newQuantity // Update the item quantity
                cartItemQuantity.text = newQuantity.toString() // Update the UI
                onCounterUpdate(item, newQuantity) // Notify the callback

            } else {
                cartItemDecrease.isEnabled = false
            }
        }
        cartItemImage.load(item.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }

    }


}