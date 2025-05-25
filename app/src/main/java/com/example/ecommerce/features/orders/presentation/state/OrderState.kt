package com.example.ecommerce.features.orders.presentation.state

import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems

data class OrderState(
    val orders: List<OrderWithItems> = emptyList(),
    val orderItem: Array<OrderItemEntity> = emptyArray(),
    val isOrdersLoading: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderState

        if (isOrdersLoading != other.isOrdersLoading) return false
        if (orders != other.orders) return false
        if (!orderItem.contentEquals(other.orderItem)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isOrdersLoading.hashCode()
        result = 31 * result + orders.hashCode()
        result = 31 * result + (orderItem?.contentHashCode() ?: 0)
        return result
    }
}
