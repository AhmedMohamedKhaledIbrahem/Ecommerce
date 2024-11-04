package com.example.ecommerce.core.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.ecommerce.R
import com.google.android.material.snackbar.Snackbar

object SnackBarCustom {
    @SuppressLint("RestrictedApi")
    fun showSnackbar(view: View, message: String, iconResId: Int? = null) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        iconResId?.let {
            val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
            val textView = snackbarLayout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            val drawable = ContextCompat.getDrawable(view.context, it)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            textView.setCompoundDrawables(drawable, null, null, null)
            textView.compoundDrawablePadding = view.context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        }
        snackbar.show()
    }
}