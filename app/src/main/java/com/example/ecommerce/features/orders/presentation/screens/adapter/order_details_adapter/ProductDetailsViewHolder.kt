package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.databinding.ItemProductDetailsBinding

class ProductDetailsViewHolder(
    binding: ItemProductDetailsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
    private val productsAmount: TextView = binding.amountProduct
    private val totalProductsPrice: TextView = binding.totalPriceProduct


    fun bind(orderItem: List<OrderItemEntity>) {
        val context = itemView.context
        val totalQuantity = orderItem.sumOf { it.quantity }
        val totalPrice = orderItem.sumOf { it.priceItem.toDouble() }
        productsAmount.text =
            context.getString(R.string.products_amount).plus(
                totalQuantity
            )
        totalProductsPrice.text =
            context.getString(R.string.total_price).plus(totalPrice)
                .plus(
                    context.getString(R.string.currency)
                )

    }

}