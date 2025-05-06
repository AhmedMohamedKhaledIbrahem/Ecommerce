package com.example.ecommerce.features.cart.presentation.screens.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCheckoutBinding


class CheckoutFragment : DialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }


}