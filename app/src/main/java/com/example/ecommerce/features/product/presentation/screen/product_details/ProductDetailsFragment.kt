package com.example.ecommerce.features.product.presentation.screen.product_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import com.example.ecommerce.features.cart.presentation.viewmodel.ICartViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
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
    private val cartViewModel: ICartViewModel by viewModels<CartViewModel>()

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
        cartState()
        productImage.load(product.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }
        favoriteButton.setOnClickListener { }
        addToCartButton.setOnClickListener {
            insertItem(product.productId)

        }
    }


    private fun cartState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartState.collect { state ->
                    cartUiState(state)
                }
            }
        }
    }

    private fun cartUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                cartLoadingState(state)
            }

            is UiState.Success -> {
                cartSuccessState(state)
            }

            is UiState.Error -> {
                cartErrorState(state)

            }
        }
    }


    private fun cartLoadingState(state: UiState.Loading) {
        when (state.source) {
            "addItem" -> {

            }
        }
    }

    private fun cartSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "addItem" -> {

            }

        }
    }


    private fun cartErrorState(state: UiState.Error) {
        when (state.source) {
            "addItem" -> {
                Log.e("cartErrorState", "cartErrorState: ${state.message}")
            }
        }
    }
    private fun insertItem(id:String){
        val addItemRequestEntity=AddItemRequestEntity(
            id = id ,
            quantity = "1"
        )
        cartViewModel.addItem(addItemParams = addItemRequestEntity)
    }


}