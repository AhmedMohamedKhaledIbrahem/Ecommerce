package com.example.ecommerce.features.product.presentation.screen.product_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.ecommerce.R
import com.google.android.material.imageview.ShapeableImageView

class ProductDetailsFragment : Fragment() {

    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView
    private lateinit var productCategory: TextView
    private lateinit var productStock: TextView
    private lateinit var productImage: ImageView
    private lateinit var favoriteButton: ShapeableImageView
    private lateinit var addToCartButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)
        init(view)

        return view
    }

    private fun init(view: View) {
        productName = view.findViewById(R.id.productName)
        productPrice = view.findViewById(R.id.productPrice)
        productDescription = view.findViewById(R.id.productDescription)
        productCategory = view.findViewById(R.id.productCategory)
        productImage = view.findViewById(R.id.productImage)
        favoriteButton = view.findViewById(R.id.favoriteButton)
        addToCartButton = view.findViewById(R.id.addToCartButton)
        productStock = view.findViewById(R.id.productStock)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        productStock.text = "Status Stock:${product.stock}"
        productName.text = "Product:${product.productName}"
        productPrice.text = "Price:${product.productPrice} EG"
        productDescription.text = product.productDescription
        productCategory.text = "Category:${product.category}"
        productImage.load(product.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }
        favoriteButton.setOnClickListener { }
        addToCartButton.setOnClickListener { }
    }


}