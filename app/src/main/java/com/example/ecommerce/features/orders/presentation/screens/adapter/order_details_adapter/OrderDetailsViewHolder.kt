package com.example.ecommerce.features.orders.presentation.screens.adapter.order_details_adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.databinding.ItemOrderDetailsBinding
import com.google.android.material.imageview.ShapeableImageView

class OrderDetailsViewHolder(
    binding: ItemOrderDetailsBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val orderItemImage: ShapeableImageView = binding.orderItemImage
    private val orderItemName: TextView = binding.orderItemName
    private val orderItemPrice: TextView = binding.orderItemPrice
    fun bind(orderItem: OrderItemEntity) {
        val context = itemView.context
        orderItemImage.load(orderItem.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }
        orderItemName.text = context.getString(R.string.product).plus(orderItem.itemName)
        orderItemPrice.text =
            context.getString(R.string.price).plus(orderItem.priceItem)
                .plus(context.getString(R.string.currency)).plus(" ")
                .plus(context.getString(R.string.Xlatter)).plus(orderItem.quantity)
    }
}