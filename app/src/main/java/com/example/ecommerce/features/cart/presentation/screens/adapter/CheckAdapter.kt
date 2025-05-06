package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemCheckOutBinding

class CheckAdapter(private val onCheckoutClick: () -> Unit) :
    RecyclerView.Adapter<CheckViewHolder>() {
    private var totalPrice: Double = 0.0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckViewHolder {
        val binding =
            ItemCheckOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckViewHolder(binding, onCheckoutClick)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: CheckViewHolder, position: Int) {
        holder.bind(totalPrice)
    }

    fun updateTotalPrice(newTotalPrice: Double) {
        totalPrice = newTotalPrice
        notifyItemChanged(0)

    }
}