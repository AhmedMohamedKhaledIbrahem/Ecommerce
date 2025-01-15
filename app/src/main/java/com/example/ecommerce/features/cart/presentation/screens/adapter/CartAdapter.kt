package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity

class CartAdapter(
    private val cartItems: List<ItemCartEntity>,
    private val onCounterUpdate: (ItemCartEntity, Int) -> Unit
) : RecyclerView.Adapter<CartViewHolder>() {

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val create = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(create, onCounterUpdate)
    }

    override fun getItemCount(): Int = cartItems.size
}