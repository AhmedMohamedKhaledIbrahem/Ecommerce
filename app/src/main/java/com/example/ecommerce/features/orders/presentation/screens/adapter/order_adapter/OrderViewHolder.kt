package com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.databinding.ItemOrderBinding

class OrderViewHolder(
    binding: ItemOrderBinding,
    private val onOrderClick: (List<OrderItemEntity>) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private val orderTagNumber = binding.orderTagNumber
    private val orderDateCreate = binding.orderDateCreate
    private val orderTotal = binding.orderTotal
    private val orderCard = binding.ordersCard
    private val statusCard = binding.statusCard
    private val statusText = binding.statusText


    fun bind(orderWithItems: OrderWithItems) {
        val context = itemView.context
        val status = orderWithItems.order.status.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
        val tagNumber = orderWithItems.order.orderTagNumber
        val totalPrice = orderWithItems.order.totalPrice
        val colorStatus = getOrderStatusColor(context, orderWithItems.order.status)
        statusCard.setCardBackgroundColor(colorStatus)
        statusText.text = context.getString(R.string.status, status)
        val dateTime = orderWithItems.order.dateCreatedOrder
        orderTagNumber.text = context.getString(R.string.order_tag_number).plus(tagNumber)
        orderDateCreate.text =
            context.getString(R.string.order_date_create).plus(dateTime)

        orderTotal.text = context.getString(R.string.order_total).plus(totalPrice).plus(" ").plus(context.getString(R.string.currency))
        orderCard.setOnClickListener {
            onOrderClick(orderWithItems.items)
        }
    }

}