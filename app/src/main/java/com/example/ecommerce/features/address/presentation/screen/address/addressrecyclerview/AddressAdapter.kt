package com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class AddressAdapter(
    private val addressItems: List<AddressItem>,
    private val onItemClickListener: (AddressItem) -> Unit

) : RecyclerView.Adapter<AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressItems[position]
        holder.fullNameAddressTextView.text = address.fullName
        holder.emailAddressTextView.text = address.emailAddress
        holder.phoneNumberTextView.text = address.phoneNumber
        holder.streetAddressTextView.text = address.streetAddress
        holder.countryTextView.text = address.country
        holder.cityTextView.text = address.city
       // holder.stateTextView.text = address.state
        holder.postCodeTextView.text = address.postCode
        holder.addressCardView.setOnClickListener {
            onItemClickListener(address)
        }
    }


    override fun getItemCount(): Int = addressItems.size

}