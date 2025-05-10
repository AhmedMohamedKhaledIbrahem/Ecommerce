package com.example.ecommerce.features.product.presentation.screen.product_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.ecommerce.R
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentProductDetailsBinding
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import com.example.ecommerce.features.cart.presentation.viewmodel.ICartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()
    private val cartViewModel: ICartViewModel by viewModels<CartViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        binding.productStock.text = getString(R.string.status_stock, product.stock)
        binding.productName.text = getString(R.string.product_name, product.productName)
        binding.productPrice.text = getString(R.string.price_eg, product.productPrice)
        binding.productDescription.text = product.productDescription
        binding.productCategory.text = getString(R.string.category, product.category)
        cartState()
        binding.productImage.load(product.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }
        binding.favoriteButton.setOnClickListener { }
        binding.addToCartButton.setOnClickListener {
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
            "addItem" -> {}
        }
    }

    private fun cartSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "addItem" -> {
                SnackBarCustom.showSnackbar(
                    view = requireView(),
                    message = getString(R.string.the_item_has_been_added_successfully)
                )
                findNavController().navigate(R.id.cartFragment)
            }

        }
    }


    private fun cartErrorState(state: UiState.Error) {
        when (state.source) {
            "addItem" -> {
                SnackBarCustom.showSnackbar(
                    view = requireView(),
                    message = state.message
                )
            }
        }
    }

    private fun insertItem(id: String) {
        val addItemRequestEntity = AddItemRequestEntity(
            id = id,
            quantity = "1"
        )
        cartViewModel.addItem(addItemParams = addItemRequestEntity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}