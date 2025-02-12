package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.google.android.material.button.MaterialButton

class CheckViewHolder(
    view: View,
    private val onCheckOutClicked: () -> Unit
) :
    RecyclerView.ViewHolder(view) {

    private val totalPriceTextView: TextView = view.findViewById(R.id.totalTextView)
    private val checkOutButton: MaterialButton = view.findViewById(R.id.buttonCheckOut)
     @SuppressLint("SetTextI18n", "DefaultLocale")
     fun bind(totalPrice: Double){
        totalPriceTextView.text = "Total Price: ${String.format("%.2f", totalPrice)} EG"
         checkOutButton.setOnClickListener {
             onCheckOutClicked.invoke()
         }
     }
}