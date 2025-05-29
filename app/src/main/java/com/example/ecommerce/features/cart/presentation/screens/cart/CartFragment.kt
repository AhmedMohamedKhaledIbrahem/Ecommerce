package com.example.ecommerce.features.cart.presentation.screens.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.manager.address.AddressManager
import com.example.ecommerce.core.manager.customer.CustomerManager
import com.example.ecommerce.core.ui.event.UiEvent
import com.example.ecommerce.core.ui.event.combinedEvents
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.features.cart.data.data_soruce.local.calculateTotalPrice
import com.example.ecommerce.features.cart.presentation.event.CartEvent
import com.example.ecommerce.features.cart.presentation.event.CheckOutEvent
import com.example.ecommerce.features.cart.presentation.screens.adapter.CartAdapter
import com.example.ecommerce.features.cart.presentation.screens.adapter.CheckAdapter
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import com.example.ecommerce.features.cart.presentation.viewmodel.CheckOutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View
    private lateinit var cartAdapter: CartAdapter
    private lateinit var checkAdapter: CheckAdapter

    @Inject
    lateinit var customerManager: CustomerManager

    @Inject
    lateinit var addressManager: AddressManager
    private val cartViewModel: CartViewModel by viewModels<CartViewModel>()
    private val checkOutViewModel by viewModels<CheckOutViewModel>()

    private var itemHashKeys: MutableList<ItemCartEntity> = mutableListOf()
    private var itemCart: ItemCartEntity? = null


    private lateinit var loadingDialog: LoadingDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        root = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        getCartWithItems()
        cartEvent()
        cartState()
        cartLoad()
        setAddressId()
        checkOutState()
        checkOutEvent()
        detectScrollEnd(binding.cartRecyclerView)

    }


    private fun initRecyclerView(items: MutableList<ItemCartEntity>) {

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        checkAdapter = CheckAdapter(
            onCheckoutClick = {
                lifecycleScope.launch {
                    checkOutViewModel.onEvent(
                        CheckOutEvent.Input.AddressId(
                            addressManager.getAddressId()
                        )
                    )
                    checkOutViewModel.onEvent(
                        CheckOutEvent.Input.CustomerId(
                            customerManager.getCustomerId()
                        )
                    )
                    checkOutViewModel.onEvent(CheckOutEvent.CheckoutButton)
                }
            },
        )
        cartAdapter = CartAdapter(
            cartItems = items,
            onIncrease = { item, newQuantity ->
                updateItemQuantity(items, item, newQuantity)
                cartViewModel.onEvent(CartEvent.Input.ItemId(item.itemId))
                cartViewModel.onEvent(CartEvent.Input.IncreaseQuantity(newQuantity))
                cartViewModel.onEvent(CartEvent.Button.IncreaseQuantity)

            },
            onDecrease = { item, newQuantity ->
                updateItemQuantity(items, item, newQuantity)
                cartViewModel.onEvent(CartEvent.Input.ItemId(item.itemId))
                cartViewModel.onEvent(CartEvent.Input.DecreaseQuantity(newQuantity))
                cartViewModel.onEvent(CartEvent.Button.DecreaseQuantity)

            },
            onDeleteItem = { item ->
                itemCart = item
                cartViewModel.onEvent(CartEvent.Input.RemoveItem(item.itemHashKey))
                cartViewModel.onEvent(CartEvent.Button.RemoveItem)
            }
        )
        updateAdapter(items)
        val totalPrice = calculateTotalPrice(items)
        checkAdapter.updateTotalPrice(totalPrice)
    }

    private fun updateItemQuantity(
        items: MutableList<ItemCartEntity>,
        item: ItemCartEntity,
        newQuantity: Int
    ) {
        val index = items.indexOf(item)
        if (index != -1) {
            items[index].quantity = newQuantity
            cartAdapter.notifyItemChanged(index)
            val totalPrice = calculateTotalPrice(items)
            checkAdapter.updateTotalPrice(totalPrice)

        }
    }

    private fun updateAdapter(items: MutableList<ItemCartEntity>) {
        if (items.isNotEmpty()) {
            binding.cartRecyclerView.adapter = ConcatAdapter(cartAdapter, checkAdapter)
        } else {
            binding.cartRecyclerView.adapter = cartAdapter
        }

    }

    private fun getCartWithItems() {
        cartViewModel.onEvent(CartEvent.LoadCart)
    }


    private fun cartEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            Log.d("TAG", "cartEvent: ${event.message}")
                            SnackBarCustom.showSnackbar(
                                view = root,
                                message = event.message
                            )
                        }


                        else -> Unit
                    }
                }

            }
        }
    }

    private fun checkOutEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkOutViewModel.checkOutEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowSnackBar -> {
                            Log.d("TAG", "cartEvent: ${event.message}")
                            SnackBarCustom.showSnackbar(
                                view = root,
                                message = event.message
                            )

                        }

                        is UiEvent.CombinedEvents -> {

                            combinedEvents(
                                events = event.events,
                                onShowSnackBar = { message, _  ->
                                    SnackBarCustom.showSnackbar(
                                        view = root,
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
                cartViewModel.cartState.collectLatest { state ->
                    itemHashKeys = state.cartWithItems.items.toMutableList()
                }
            }
        }
    }

    private fun cartLoad() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartLoadState.collect { state ->
                    if (!state.isGetLoading) {
                        initRecyclerView(itemHashKeys)

                    }
                    if (::cartAdapter.isInitialized && ::checkAdapter.isInitialized) {
                        cartAdapter.setRemoveLoadingState(state.isRemoveLoading)
                        checkAdapter.setLoadingState(state.isRemoveLoading)
                    }

                    if (!state.isRemoveLoading) {
                        deleteItem()
                    }


                }
            }
        }
    }

    private fun checkOutState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkOutViewModel.checkOutState.collect { state ->

                    if (::cartAdapter.isInitialized && ::checkAdapter.isInitialized) {
                        Log.d("TAG", "checkOutState: ${state.isCheckingOut}")
                        checkAdapter.setLoadingState(state.isCheckingOut)
                    }


                }
            }
        }
    }

    private fun deleteItem() {
        val position = itemHashKeys.indexOf(itemCart)
        if (position != -1) {
            getCartWithItems()
            val totalPrice = calculateTotalPrice(itemHashKeys)
            checkAdapter.updateTotalPrice(totalPrice)

            if (itemHashKeys.isEmpty()) {
                binding.cartRecyclerView.adapter = cartAdapter
            }
        }
    }

    private fun setAddressId() {
        lifecycleScope.launch {
            val addressId = addressManager.getAddressId()
            Log.d("TAG", "setAddressId: $addressId")
            if (addressId == -1) return@launch
            checkOutViewModel.onEvent(
                CheckOutEvent.Input.AddressId(
                    addressId
                )
            )
            checkOutViewModel.onEvent(
                CheckOutEvent.Input.CustomerId(
                    customerManager.getCustomerId()
                )
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}