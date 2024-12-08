package com.example.ecommerce.features.userprofile.presentation.screens.settingscreen.settingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class SettingAdapter(
    private val settingItems: List<SettingItem>,
    private val onItemClickListener: (SettingItem) -> Unit
) : RecyclerView.Adapter<SettingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return SettingViewHolder(view)
    }

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
    }
}