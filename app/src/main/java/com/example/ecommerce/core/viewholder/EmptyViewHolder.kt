package com.example.ecommerce.core.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.google.android.material.imageview.ShapeableImageView


class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val emptyTextView: TextView = view.findViewById(R.id.emptyCartText)
    private val emptyImageView: ShapeableImageView = view.findViewById(R.id.emptyCartImage)
    fun bind(
        emptyViewEntity: EmptyViewEntity
    ) {
        val context = itemView.context
        emptyTextView.text = context.getString(R.string.empty_message, emptyViewEntity.emptyText)
        emptyImageView.setImageResource(emptyViewEntity.emptyImage)

    }
}