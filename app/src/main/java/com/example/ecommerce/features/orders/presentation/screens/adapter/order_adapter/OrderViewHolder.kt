package com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems

class OrderViewHolder(
    view: View,
    private val onOrderClick: (List<OrderItemEntity>) -> Unit,
    private val context: Context
) : RecyclerView.ViewHolder(view) {
    private val orderTagNumber = view.findViewById<TextView>(R.id.orderTagNumber)
    private val orderDateCreate = view.findViewById<TextView>(R.id.orderDateCreate)
    private val orderStatus = view.findViewById<TextView>(R.id.orderStatus)
    private val orderTotal = view.findViewById<TextView>(R.id.orderTotal)
    private val orderCard = view.findViewById<View>(R.id.ordersCard)

    @SuppressLint("SetTextI18n")
    fun bind(orderWithItems: OrderWithItems) {
        val dateTime = removeTimeFromDate(orderWithItems.order.dateCreatedOrder)
        orderTagNumber.text =
            context.getString(R.string.order_tag_number) + orderWithItems.order.orderTagNumber
        orderDateCreate.text =
            context.getString(R.string.order_date_create) + dateTime
        orderStatus.text = context.getString(R.string.order_status) + orderWithItems.order.status
        orderTotal.text = context.getString(R.string.order_total) + orderWithItems.order.totalPrice
        orderCard.setOnClickListener {
            onOrderClick(orderWithItems.items)
        }
    }
    fun removeTimeFromDate(dateTime: String): String {
        return dateTime.replace(Regex("T\\d{2}:\\d{2}:\\d{2}"), "")
    }
}