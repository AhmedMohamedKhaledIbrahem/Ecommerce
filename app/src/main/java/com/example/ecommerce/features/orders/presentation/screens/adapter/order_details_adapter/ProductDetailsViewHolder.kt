package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity

class ProductDetailsViewHolder(
    view: View,
    private val context: Context
) : RecyclerView.ViewHolder(view) {
    private val productsAmount: TextView = itemView.findViewById(R.id.amountProduct)
    private val totalProductsPrice: TextView = itemView.findViewById(R.id.totalPriceProduct)

    @SuppressLint("SetTextI18n")
    fun bind(orderItem: List<OrderItemEntity>) {
        productsAmount.text =
            context.getString(R.string.products_amount) + "${orderItem.sumOf { it.quantity }}"
        totalProductsPrice.text =
            context.getString(R.string.total_price) + "${orderItem.sumOf { it.priceItem.toDouble() }} " + context.getString(
                R.string.currency
            )

    }

}