package com.example.ecommerce.features.address.presentation.screen.address.addressrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.databinding.ItemAddressBinding

class AddressAdapter(
    private val addressItems: List<CustomerAddressEntity>,
    private val onDeleteClickListener: (CustomerAddressEntity) -> Unit,
    private val onEditClickListener: (CustomerAddressEntity) -> Unit,
    private val onCardClickListener: (CustomerAddressEntity) -> Unit,

    ) : RecyclerView.Adapter<AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(
            binding,
            onDeleteClickListener,
            onEditClickListener,
            onCardClickListener
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressItems[position]
        holder.bind(address)

    }


    override fun getItemCount(): Int = addressItems.size

}