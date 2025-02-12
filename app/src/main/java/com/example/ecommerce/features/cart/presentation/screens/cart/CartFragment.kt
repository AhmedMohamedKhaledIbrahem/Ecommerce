package com.example.ecommerce.features.cart.presentation.screens.cart

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.R
import com.example.ecommerce.core.customer.CustomerManager
import com.example.ecommerce.core.database.data.entities.address.CustomerAddressEntity
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.fragment.LoadingDialogFragment
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.address.domain.entites.BillingInfoRequestEntity
import com.example.ecommerce.features.address.domain.entites.ShippingInfoRequestEntity
import com.example.ecommerce.features.address.presentation.viewmodel.AddressViewModel
import com.example.ecommerce.features.address.presentation.viewmodel.IAddressViewModel
import com.example.ecommerce.features.cart.data.data_soruce.local.calculateTotalPrice
import com.example.ecommerce.features.cart.presentation.BottomSpacingDecoration
import com.example.ecommerce.features.cart.presentation.screens.adapter.CartAdapter
import com.example.ecommerce.features.cart.presentation.screens.adapter.CheckAdapter
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import com.example.ecommerce.features.cart.presentation.viewmodel.ICartViewModel
import com.example.ecommerce.features.orders.domain.entities.LineItemRequestEntity
import com.example.ecommerce.features.orders.domain.entities.OrderRequestEntity
import com.example.ecommerce.features.orders.presentation.viewmodel.IOrderViewModel
import com.example.ecommerce.features.orders.presentation.viewmodel.OrderViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartSwipeRefreshLayout: SwipeRefreshLayout
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
    private val loadingDialog by lazy {
        LoadingDialogFragment().getInstance(parentFragmentManager)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        initView(view)
        initViewModel()
        return view
    }

    private fun initView(view: View) {
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartSwipeRefreshLayout = view.findViewById(R.id.cartSwipeRefreshViewLayout)
        root = view
    }

    private fun initViewModel() {
        detectViewModel =
            ViewModelProvider(requireActivity())[DetectScrollEndViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCartWithItems()
        cartState()
        addressState()
        orderState()
        swipeItem()
       // onSwipeRefreshListener()
        detectScrollEnd()

    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun initRecyclerView(items: MutableList<ItemCartEntity>) {
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        checkAdapter = CheckAdapter {
            getAddressByCustomerId()
        }
        cartAdapter = CartAdapter(
            items,
        ) { item, newQuantity ->
            updateItemQuantity(items, item, newQuantity)
        }
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
        println(items)
        if (items.isEmpty()) {
            cartRecyclerView.adapter = cartAdapter
        } else {
            cartRecyclerView.adapter = ConcatAdapter(cartAdapter, checkAdapter)
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
                addressViewModel.addressState.collect { state ->
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
                    Log.e("is null??","no")
                    val billingEntity = billingInfoRequestEntity(address)
                    val shippingEntity = shippingInfoRequestEntity(address)
                    val lineItemRequestEntity: List<LineItemRequestEntity> =
                        lineItemRequestEntities()
                    val orderRequestEntity =
                        orderRequestEntity(billingEntity, shippingEntity, lineItemRequestEntity)
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
        }
    }

    private fun orderSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "createOrder" -> {
                loadingDialog.dismissLoading()
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = getString(R.string.order_has_been_created_successfully)
                )

            }
        }
    }

    private fun orderErrorState(state: UiState.Error) {
        when (state.source) {
            "createOrder" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }
        }
    }

    private fun getAddressByCustomerId() {
        addressViewModel.getAddressById(id = customerManager.getCustomerId())
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
        }
    }

    private fun cartSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "getCart" -> {
                val cartWithItems = state.data as? CartWithItems
                if (cartWithItems != null) {
                    itemHashKeys = cartWithItems.items.toMutableList()
                    initRecyclerView(itemHashKeys)
                }
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
        }
    }

    private fun orderRequestEntity(
        billingEntity: BillingInfoRequestEntity,
        shippingEntity: ShippingInfoRequestEntity,
        lineItemRequestEntity: List<LineItemRequestEntity>
    ): OrderRequestEntity {
        val orderRequestEntity = OrderRequestEntity(
            paymentMethod = "cod",
            paymentMethodTitle = "Cash On Delivery",
            setPaid = false,
            billing = billingEntity,
            shipping = shippingEntity,
            lineItems = lineItemRequestEntity,
            customerId = customerManager.getCustomerId()
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

    private fun shippingInfoRequestEntity(address: CustomerAddressEntity): ShippingInfoRequestEntity {
        val shippingEntity = ShippingInfoRequestEntity(
            firstName = address.firstName,
            lastName = address.lastName,
            address = address.address,
            city = address.city,
            state = address.state,
            country = address.country,
            postCode = address.zipCode,

            )
        return shippingEntity
    }

    private fun billingInfoRequestEntity(address: CustomerAddressEntity): BillingInfoRequestEntity {
        val billingEntity = BillingInfoRequestEntity(
            firstName = address.firstName,
            lastName = address.lastName,
            address = address.address,
            city = address.city,
            state = address.state,
            country = address.country,
            postCode = address.zipCode,
            phone = address.phone,
            email = address.email,
        )
        return billingEntity
    }

    private fun onSwipeRefreshListener() {
        cartSwipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                cartViewModel.updateItemsCart()
                cartSwipeRefreshLayout.isRefreshing = false


            }
        }
    }


    private fun swipeItem() {
        val itemTouchHelper = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            val key = itemHashKeys[position].itemHashKey
                            cartViewModel.removeItem(keyItem = key)
                            itemHashKeys.removeAt(position)
                            cartAdapter.notifyItemRemoved(position)
                            if (itemHashKeys.isEmpty()) {
                                cartRecyclerView.adapter = cartAdapter
                            }

                        }


                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemWidth = viewHolder.itemView.width
                val maxSwipeDistance = itemWidth / 5f // Half of the item width
                val limitedDx = dX.coerceIn(-maxSwipeDistance, maxSwipeDistance)

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    limitedDx,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.vermilion
                        )
                    ).addSwipeLeftActionIcon(R.drawable.baseline_block_flipped_24)
                    .addCornerRadius(1, 30)

                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    limitedDx,
                    dY,
                    actionState,
                    isCurrentlyActive
                )


            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(cartRecyclerView)

    }

    private fun detectScrollEnd() {
        cartRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager =
                    recyclerView.layoutManager as? LinearLayoutManager ?: return
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && dy > 0) {
                    recyclerView.post {
                        val lastView =
                            recyclerView.findViewHolderForAdapterPosition(totalItemCount - 1)?.itemView
                        lastView?.let {
                            val lastItemHeight = it.height
                            println(lastItemHeight)
                            removeItemDecoration(recyclerView)
                            recyclerView.addItemDecoration(
                                BottomSpacingDecoration(
                                    lastItemHeight
                                )
                            )
                            recyclerView.clipToPadding = false
                        }
                    }
                }
            }
        })
    }

    private fun removeItemDecoration(recyclerView: RecyclerView) {
        for (i in 0 until recyclerView.itemDecorationCount) {
            val decoration = recyclerView.getItemDecorationAt(i)
            if (decoration is BottomSpacingDecoration) {
                recyclerView.removeItemDecoration(decoration)
                break
            }
        }
    }

}