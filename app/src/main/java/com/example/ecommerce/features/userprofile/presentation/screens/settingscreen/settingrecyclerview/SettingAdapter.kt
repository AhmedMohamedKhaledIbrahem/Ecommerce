package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemSettingBinding

class SettingAdapter(
    private var settingItems: List<SettingItem>,
    private val onItemClickListener: (SettingItem) -> Unit,
    private val onSwitchChangeListener: (SettingItem, Boolean) -> Unit
) : RecyclerView.Adapter<SettingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingViewHolder(binding)
    }
    val items: List<SettingItem> get() = settingItems
    override fun getItemCount(): Int = settingItems.size

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val setting = settingItems[position]
        holder.settingImage.setImageResource(setting.imageResId)
        holder.settingTitle.text = setting.title
        holder.settingCardView.setOnClickListener {
            onItemClickListener(setting)
        }
        if (!setting.enableSwitch) {
            holder.settingSwitch.visibility = View.GONE
        } else {
            holder.settingSwitch.visibility = View.VISIBLE
        }
        holder.settingSwitch.setOnCheckedChangeListener(null)
        holder.settingSwitch.isChecked = setting.isChecked
        holder.settingSwitch.setOnCheckedChangeListener { _, isChecked ->
            setting.isChecked = isChecked
            onSwitchChangeListener(setting, isChecked)
        }

    }

    fun updateItemCheckedState(position: Int, isChecked: Boolean) {
        val setting = settingItems[position]
        if (setting.isChecked != isChecked) {
            val updatedItems = settingItems.toMutableList().apply {
                this[position] = this[position].copy(isChecked = isChecked)
            }
            settingItems = updatedItems
            notifyItemChanged(position)
        }
    }
}