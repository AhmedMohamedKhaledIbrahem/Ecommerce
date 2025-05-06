package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Currency
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.databinding.ItemCartBinding
import com.google.android.material.imageview.ShapeableImageView

class CartViewHolder(
    binding: ItemCartBinding,
    private val onCounterUpdate: (ItemCartEntity, Int) -> Unit,
    private val onDeleteItem: (ItemCartEntity) -> Unit

) : RecyclerView.ViewHolder(binding.root) {
    private val cartItemImage: ShapeableImageView = binding.cartItemImage
    private val cartItemName: TextView = binding.nameItemCartText
    private val cartItemPrice: TextView = binding.priceItemCartText
    private val cartDeleteImage: ImageView = binding.deleteImage
    private val cartItemQuantity: TextView = binding.cartItemPriceTextView
    private val cartItemIncrease: ImageView = binding.itemCartIncreaseCounter
    private val cartItemDecrease: ImageView = binding.itemCartDecreaseCounter


    @SuppressLint("SetTextI18n")
    fun bind(item: ItemCartEntity) {
        cartItemName.text = item.name
        cartItemPrice.text = item.price.plus(".00") + " $Currency"
        cartItemQuantity.text = item.quantity.toString()
        cartItemIncrease.setOnClickListener {
            if (item.quantity < 10) {
                val newQuantity = item.quantity + 1
                item.quantity = newQuantity
                cartItemQuantity.text = newQuantity.toString()
                onCounterUpdate(item, newQuantity)

            }

        }
        cartDeleteImage.setOnClickListener { onDeleteItem(item) }
        cartItemDecrease.setOnClickListener {
            if (item.quantity > 1) {
                val newQuantity = item.quantity - 1
                item.quantity = newQuantity
                cartItemQuantity.text = newQuantity.toString()
                onCounterUpdate(item, newQuantity)

            }
        }
        cartItemImage.load(item.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }

    }


}