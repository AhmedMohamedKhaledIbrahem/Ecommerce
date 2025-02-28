package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.google.android.material.imageview.ShapeableImageView

class OrderDetailsViewHolder(
    view: View,
    private val context: Context
) : RecyclerView.ViewHolder(view) {
    private val orderItemImage: ShapeableImageView = itemView.findViewById(R.id.orderItemImage)
    private val orderItemName: TextView = itemView.findViewById(R.id.orderItemName)
    private val orderItemPrice: TextView = itemView.findViewById(R.id.orderItemPrice)


    @SuppressLint("SetTextI18n")
    fun bind(orderItem: OrderItemEntity) {

        orderItemImage.load(orderItem.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }
        orderItemName.text = "${context.getString(R.string.product)}${orderItem.itemName}"
        orderItemPrice.text =
            "${context.getString(R.string.price)}${orderItem.priceItem} ${context.getString(R.string.currency)} x${orderItem.quantity}"
    }
}