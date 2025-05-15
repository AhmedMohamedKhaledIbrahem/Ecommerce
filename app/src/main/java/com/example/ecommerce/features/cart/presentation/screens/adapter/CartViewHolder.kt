package com.example.ecommerce.features.cart.presentation.screens.adapter

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.constants.Currency
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.databinding.ItemCartBinding
import com.google.android.material.imageview.ShapeableImageView

class CartViewHolder(
    binding: ItemCartBinding,
    private val onIncrease: (ItemCartEntity, Int) -> Unit,
    private val onDecrease: (ItemCartEntity, Int) -> Unit,
    private val onDeleteItem: (ItemCartEntity) -> Unit

) : RecyclerView.ViewHolder(binding.root) {
    private var rotateAnimator: ObjectAnimator? = null

    private val cartItemImage: ShapeableImageView = binding.cartItemImage
    private val cartItemName: TextView = binding.nameItemCartText
    private val cartItemPrice: TextView = binding.priceItemCartText
    private val cartDeleteImage: ImageView = binding.deleteImage
    private val cartItemQuantity: TextView = binding.cartItemPriceTextView
    private val cartItemIncrease: ImageView = binding.itemCartIncreaseCounter
    private val cartItemDecrease: ImageView = binding.itemCartDecreaseCounter


    fun bind(item: ItemCartEntity, isRemoveLoading: Boolean) {
        removeState(isRemoveLoading)
        val context = itemView.context
        cartItemName.text = item.name
        cartItemPrice.text = item.price.plus(context.getString(R.string._00)).plus(" $Currency")
        cartItemQuantity.text = item.quantity.toString()
        cartItemIncrease.setOnClickListener {
            if (item.quantity < 10) {
                val newQuantity = item.quantity + 1
                item.quantity = newQuantity
                cartItemQuantity.text = newQuantity.toString()
                onIncrease(item, newQuantity)

            }

        }
        cartDeleteImage.setOnClickListener {
            onDeleteItem(item)
        }
        cartItemDecrease.setOnClickListener {
            if (item.quantity > 1) {
                val newQuantity = item.quantity - 1
                item.quantity = newQuantity
                cartItemQuantity.text = newQuantity.toString()
                onDecrease(item, newQuantity)

            }
        }
        cartItemImage.load(item.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }

    }

    private fun removeState(isRemoveLoading: Boolean) {
        if (isRemoveLoading) {
            cartItemImage.isEnabled = false
            cartItemName.isEnabled = false
            cartItemPrice.isEnabled = false
            cartItemQuantity.isEnabled = false
            cartItemIncrease.isEnabled = false
            cartItemDecrease.isEnabled = false
            cartItemImage.isEnabled = false
            startDeleteImageButtonAnimation()
            startRotateAnimation(cartItemImage)
        } else {
            cartItemImage.isEnabled = true
            cartItemName.isEnabled = true
            cartItemPrice.isEnabled = true
            cartItemQuantity.isEnabled = true
            cartItemIncrease.isEnabled = true
            cartItemDecrease.isEnabled = true
            cartItemImage.isEnabled = true
            stopRotateAnimation(cartItemImage)
            stopDeleteImageButtonAnimation()
        }
    }

    private fun startDeleteImageButtonAnimation() {
        cartDeleteImage.apply {
            animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(0.3f)
                .setDuration(300)
                .start()
            isEnabled = false

        }

    }

    private fun stopDeleteImageButtonAnimation() {
        cartDeleteImage.apply {
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(300)
                .start()
            isEnabled = true
        }

    }

    private fun startRotateAnimation(view: View) {
        rotateAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun stopRotateAnimation(view: View) {
        rotateAnimator?.cancel()
        view.rotation = 0f
    }

}