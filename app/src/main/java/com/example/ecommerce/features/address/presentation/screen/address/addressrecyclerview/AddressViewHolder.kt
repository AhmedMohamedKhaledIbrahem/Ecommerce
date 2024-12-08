package com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val addressCardView: CardView = itemView.findViewById(R.id.addressCardView)
    val fullNameAddressTextView: TextView = itemView.findViewById(R.id.fullNameAddressTextView)
    val emailAddressTextView: TextView = itemView.findViewById(R.id.emailAddressTextView)
    val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberTextView)
    val streetAddressTextView: TextView = itemView.findViewById(R.id.streetAddressTextView)
    val countryTextView: TextView = itemView.findViewById(R.id.countryTextView)
    val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)
    val stateTextView: TextView = itemView.findViewById(R.id.stateTextView)
    val postCodeTextView: TextView = itemView.findViewById(R.id.postCodeTextView)


}