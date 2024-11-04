package com.example.ecommerce.core.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.ecommerce.R
import com.google.android.material.snackbar.Snackbar


class SnackBarFragment : Fragment() {
    companion object {
        private var instance: SnackBarFragment? = null

        fun getInstance(): SnackBarFragment {
            if (instance == null) {
                instance = SnackBarFragment()
            }
            return instance!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_snackbar, container, false)
    }

    @SuppressLint("RestrictedApi", "InflateParams")
    fun showCustomSnackBar(view:View,message:String, showIcon:Boolean =false, iconId:Int?=null){
            val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            val snackBarView = snackBar.view as Snackbar.SnackbarLayout

            val customView = layoutInflater.inflate(R.layout.custom_snackbar, null)
            val messageTextView = customView.findViewById<TextView>(R.id.textSnackBarCustom)
            val iconImageView = customView.findViewById<ImageView>(R.id.iconSnackBarCustom)
            messageTextView.text = message
            if (showIcon && iconId != null) {
                iconImageView.setImageResource(iconId)
                iconImageView.visibility = View.VISIBLE
            } else {
                iconImageView.visibility = View.GONE
            }
            snackBarView.addView(customView)
            snackBar.show()

    }


}