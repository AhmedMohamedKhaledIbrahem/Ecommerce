package com.example.ecommerce.core.database.data.entities.orders

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderWithItems(
    @Embedded val order: OrderTagEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
) : Parcelable