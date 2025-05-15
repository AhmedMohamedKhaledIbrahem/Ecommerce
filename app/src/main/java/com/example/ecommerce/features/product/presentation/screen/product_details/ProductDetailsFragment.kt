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
import com.example.ecommerce.core.constants.quantity
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.databinding.FragmentProductDetailsBinding
import com.example.ecommerce.features.cart.domain.entities.AddItemRequestEntity
import com.example.ecommerce.features.cart.presentation.event.CartEvent
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val cartViewModel: CartViewModel by viewModels<CartViewModel>()
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
        productDetails(product)
        cartEvent()
        cartState()
    }

    private fun productDetails(product: ProductDetails) {
        binding.productStock.text = getString(R.string.status_stock, product.stock)
        binding.productName.text = getString(R.string.product_name, product.productName)
        binding.productPrice.text = getString(R.string.price_eg, product.productPrice)
        binding.productDescription.text = product.productDescription
        binding.productCategory.text = getString(R.string.category, product.category)
        binding.productImage.load(product.image) {
            crossfade(true)
            placeholder(R.drawable.round_placeholder_24)
            error(R.drawable.baseline_wifi_off_24)
        }
        binding.addToCartButton.setOnClickListener {
            insertItem(product.productId)
        }
    }


    private fun cartEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            SnackBarCustom.showSnackbar(
                                view = binding.root,
                                message = event.message,
                            )
                        }

                        is UiEvent.CombinedEvents -> {
                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = { message ->
                                    SnackBarCustom.showSnackbar(
                                        view = binding.root,
                                        message = message
                                    )
                                },
                                onNavigate = { destinationId, _ ->
                                    findNavController().navigate(destinationId)
                                }
                            )
                        }

                        else -> Unit

                    }
                }
            }
        }
    }

    private fun cartState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartLoadState.collect { state ->
                    if (state.isAddLoading) {
                        binding.addToCartButton.text = ""
                        binding.addToCartButton.isEnabled = false
                        binding.addTOCartButtonProgress.visibility = View.VISIBLE
                    } else {
                        binding.addToCartButton.text = getString(R.string.add_to_cart)
                        binding.addToCartButton.isEnabled = true
                        binding.addTOCartButtonProgress.visibility = View.GONE
                    }


                }
            }
        }
    }

    private fun insertItem(id: String) {
        val addItemRequestEntity = AddItemRequestEntity(
            id = id,
            quantity = quantity
        )
        cartViewModel.onEvent(CartEvent.Input.AddItem(addItemParams = addItemRequestEntity))
        cartViewModel.onEvent(CartEvent.Button.AddToCart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}