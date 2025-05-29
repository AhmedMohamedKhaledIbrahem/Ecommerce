package com.example.ecommerce.core.viewholder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.EmptyScreenBinding
import com.google.android.material.imageview.ShapeableImageView


class EmptyViewHolder(binding: EmptyScreenBinding) : RecyclerView.ViewHolder(binding.root) {
    private val emptyTextView: TextView = binding.emptyScreenText
    private val emptyImageView: ShapeableImageView = binding.emptyScreenImage
    fun bind(
        emptyViewEntity: EmptyViewEntity
    ) {
        val context = itemView.context
        emptyTextView.text = context.getString(R.string.empty_message, emptyViewEntity.emptyText)
        emptyImageView.setImageResource(emptyViewEntity.emptyImage)

    }
}