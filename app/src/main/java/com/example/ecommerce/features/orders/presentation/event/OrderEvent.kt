package com.example.ecommerce.features.orders.presentation.event

import com.example.ecommerce.core.database.data.entities.orders.OrderItemEntity

sealed class OrderEvent {

    data class OnOrderItemCardClick(val value :  Array<OrderItemEntity>): OrderEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as OnOrderItemCardClick

            return value.contentEquals(other.value)
        }
        override fun hashCode(): Int {
            return value.contentHashCode()
        }
    }
    data object OnOrderCardClick : OrderEvent()

    data object LoadOrders : OrderEvent()
}