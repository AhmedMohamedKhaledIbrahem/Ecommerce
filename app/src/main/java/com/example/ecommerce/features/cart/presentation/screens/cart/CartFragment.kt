package com.example.ecommerce.features.cart.presentation.screens.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.customer.CustomerManager
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.presentation.viewmodel.address.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.address.IAddressViewModel
import com.example.ecommerce.features.cart.data.data_soruce.local.calculateTotalPrice
import com.example.ecommerce.features.cart.presentation.screens.adapter.CartAdapter
import com.example.ecommerce.features.cart.presentation.screens.adapter.CheckAdapter
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import com.example.ecommerce.features.cart.presentation.viewmodel.ICartViewModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderResponseEntity
import com.example.ecommerce.features.orders.presentation.viewmodel.IOrderViewModel
import com.example.ecommerce.features.orders.presentation.viewmodel.OrderViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private lateinit var checkAdapter: CheckAdapter

    @Inject
    lateinit var customerManager: CustomerManager
    private lateinit var detectViewModel: DetectScrollEndViewModel
    private lateinit var root: View
    private var itemHashKeys: MutableList<ItemCartEntity> = mutableListOf()
    private val cartViewModel: ICartViewModel by viewModels<CartViewModel>()
    private val addressViewModel: IAddressViewModel by viewModels<AddressViewModel>()
    private val orderViewModel: IOrderViewModel by viewModels<OrderViewModel>()
    private lateinit var loadingDialog: LoadingDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        root = binding.root

        initViewModel()
        return root
    }

    private fun initViewModel() {
        detectViewModel =
            ViewModelProvider(requireActivity())[DetectScrollEndViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialogFragment.getInstance(childFragmentManager)
        getCartWithItems()
        cartState()
        addressState()
        orderState()
        detectScrollEnd(binding.cartRecyclerView)

    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun initRecyclerView(items: MutableList<ItemCartEntity>) {
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        checkAdapter = CheckAdapter {
            getAddressByCustomerId()
        }
        cartAdapter = CartAdapter(
            items,
            onCounterUpdate = { item, newQuantity ->
                updateItemQuantity(items, item, newQuantity)
            },
            onDeleteItem = { item ->
                val position = items.indexOf(item)
                if (position != -1) {
                    cartViewModel.removeItem(item.itemHashKey)
                    items.removeAt(position)
                    cartAdapter.notifyItemRemoved(position)
                    val totalPrice = calculateTotalPrice(items)
                    checkAdapter.updateTotalPrice(totalPrice)
                    if (items.isEmpty()) {
                        binding.cartRecyclerView.adapter = cartAdapter
                    }
                }

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
            updateItemQuantityViewModel(item.itemId, newQuantity)
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
        cartViewModel.getCart()
    }

    private fun updateItemQuantityViewModel(itemId: Int, newQuantity: Int) {
        cartViewModel.updateQuantity(itemId = itemId, newQuantity = newQuantity)
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

    private fun addressState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addressViewModel.addressEvent.collect { state ->
                    addressUiState(state)
                }
            }
        }
    }

    private fun orderState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.orderState.collect { state ->
                    orderUiState(state)
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

    private fun addressUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                addressLoadingState(state)
            }

            is UiState.Success -> {
                addressSuccessState(state)
            }

            is UiState.Error -> {
                addressErrorState(state)
            }
        }
    }

    private fun orderUiState(state: UiState<Any>) {
        when (state) {
            is UiState.Loading -> {
                orderLoadingState(state)
            }

            is UiState.Success -> {
                orderSuccessState(state)
            }

            is UiState.Error -> {
                orderErrorState(state)
            }
        }
    }

    private fun addressLoadingState(state: UiState.Loading) {
        when (state.source) {
            "getAddressById" -> {
                loadingDialog.showLoading(fragmentManager = parentFragmentManager)
            }
        }
    }

    private fun addressSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "getAddressById" -> {
                val address = state.data as? CustomerAddressEntity
                if (address != null) {

                    val billingEntity = billingInfoRequestEntity(address)
                    val lineItemRequestEntity: List<LineItemRequestEntity> =
                        lineItemRequestEntities()
                    val orderRequestEntity =
                        orderRequestEntity(billingEntity, lineItemRequestEntity)

                    createOrder(orderRequestEntity)
                }

            }
        }
    }


    private fun addressErrorState(state: UiState.Error) {
        when (state.source) {
            "getAddressById" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }
        }
    }

    private fun orderLoadingState(state: UiState.Loading) {
        when (state.source) {
            "createOrder" -> {
            }

            "saveOrderLocally" -> {

            }
        }
    }

    private fun orderSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "createOrder" -> {
                val orderResponseEntity = state.data as? OrderResponseEntity ?: return
                orderViewModel.saveOrderLocally(orderResponseEntity = orderResponseEntity)
            }

            "saveOrderLocally" -> {
                loadingDialog.dismissLoading()
                cartViewModel.clearCart()
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = getString(R.string.order_has_been_created_successfully)
                )
                findNavController().navigate(R.id.ordersFragment)
            }
        }
    }

    private fun orderErrorState(state: UiState.Error) {
        when (state.source) {
            "createOrder" -> {
                loadingDialog.dismissLoading()
                Log.e("orderErrorState", "orderErrorState: ${state.message}")
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }

            "saveOrderLocally" -> {
                loadingDialog.dismissLoading()
                Log.e("orderErrorState", "orderErrorState: ${state.message}")
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }
        }
    }

    private fun getAddressByCustomerId() {
        //addressViewModel.getAddress(id = customerManager.getCustomerId())
    }

    private fun createOrder(orderRequestEntity: OrderRequestEntity) {
        orderViewModel.createOrder(orderRequestEntity)
    }

    private fun cartLoadingState(state: UiState.Loading) {
        when (state.source) {
            "getCart" -> {}
            "removeItem" -> {}
            "updateItemsCart" -> {}
            "updateQuantity" -> {}
            "clearCart" -> {}
        }
    }

    private fun cartSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "getCart" -> {
                val cartWithItems = state.data as? CartWithItems


                if (cartWithItems != null) {
                    itemHashKeys = cartWithItems.items.toMutableList()
                }
                initRecyclerView(itemHashKeys)


            }

            "removeItem" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = getString(R.string.the_item_has_been_removed_successfully)
                )
            }

            "updateItemsCart" -> {
                cartViewModel.getCart()
            }

            "updateQuantity" -> {}
            "clearCart" -> {}

        }
    }


    private fun cartErrorState(state: UiState.Error) {
        when (state.source) {
            "getCart" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
                Log.e("cartErrorGetCartState", "cartErrorState: ${state.message}")
            }

            "removeItem" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
                Log.e("cartErrorRemoveItemState", "cartErrorState: ${state.message}")
            }

            "updateItemsCart" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )

            }

            "updateQuantity" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }

            "clearCart" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }
        }
    }

    private fun orderRequestEntity(
        billingEntity: BillingInfoRequestEntity,
        lineItemRequestEntity: List<LineItemRequestEntity>
    ): OrderRequestEntity {
        var customerId = 0
        lifecycleScope.launch {
            customerId = customerManager.getCustomerId()
        }
        val orderRequestEntity = OrderRequestEntity(
            paymentMethod = "cod",
            paymentMethodTitle = "Cash On Delivery",
            setPaid = false,
            billing = billingEntity,
            lineItems = lineItemRequestEntity,
            customerId = customerId
        )
        return orderRequestEntity
    }

    private fun lineItemRequestEntities(): List<LineItemRequestEntity> {
        val lineItemRequestEntity: List<LineItemRequestEntity> =
            itemHashKeys.map { item ->
                LineItemRequestEntity(
                    productId = item.itemId,
                    quantity = item.quantity
                )
            }
        return lineItemRequestEntity
    }


    private fun billingInfoRequestEntity(address: CustomerAddressEntity): BillingInfoRequestEntity {
        val billingEntity = BillingInfoRequestEntity(
            firstName = address.firstName,
            lastName = address.lastName,
            address = address.address,
            city = address.city,
            country = address.country,
            postCode = address.zipCode,
            phone = address.phone,
            email = address.email,
        )
        return billingEntity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}