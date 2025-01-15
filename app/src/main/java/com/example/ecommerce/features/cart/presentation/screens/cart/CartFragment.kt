package com.example.ecommerce.features.cart.presentation.screens.cart

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.cart.CartWithItems
import com.example.ecommerce.core.database.data.entities.cart.ItemCartEntity
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.cart.data.data_soruce.local.calculateTotalPrice
import com.example.ecommerce.features.cart.presentation.screens.adapter.CartAdapter
import com.example.ecommerce.features.cart.presentation.viewmodel.CartViewModel
import com.example.ecommerce.features.cart.presentation.viewmodel.ICartViewModel
import com.example.ecommerce.features.product.presentation.viewmodel.DetectScrollEndViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var cartAdapter: CartAdapter
    private lateinit var checkoutButton: MaterialButton
    private lateinit var totalPriceTextView: TextView
    private lateinit var detectViewModel: DetectScrollEndViewModel
    private var itemHashKeys: MutableList<ItemCartEntity> = mutableListOf()
    private val cartViewModel: ICartViewModel by viewModels<CartViewModel>()
    private lateinit var root: View

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
        totalPriceTextView = view.findViewById(R.id.totalTextView)
        checkoutButton = view.findViewById(R.id.buttonCheckOut)
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
        swipeItem()
        checkoutButtonSetOnClickListener()
        onSwipeRefreshListener()
        detectScrollEnd()

    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun initRecyclerView(items: MutableList<ItemCartEntity>) {
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartAdapter = CartAdapter(
            items,
        ) { item, newQuantity ->

            val index = items.indexOf(item)
            if (index != -1) {
                Log.e("CartFragment", "initRecyclerView: $newQuantity")
                items[index].quantity = newQuantity
                cartAdapter.notifyItemChanged(index)
                cartAdapter.notifyItemChanged(index)
                val totalPrice = calculateTotalPrice(items)
                totalPriceTextView.text = "Total Price: ${String.format("%.2f", totalPrice)} EG"
                updateItemQuantityViewModel(item.itemId, newQuantity)
            }

        }

        cartRecyclerView.adapter = cartAdapter
        val totalPrice = calculateTotalPrice(items)
        totalPriceTextView.text = "Total Price: ${String.format("%.2f", totalPrice)} EG"
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


    private fun onSwipeRefreshListener() {
        cartSwipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                cartViewModel.updateItemsCart()
                cartSwipeRefreshLayout.isRefreshing = false


            }
        }
    }

    private fun checkoutButtonSetOnClickListener() {
        checkoutButton.setOnClickListener {

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
                    c, recyclerView, viewHolder, limitedDx, dY, actionState, isCurrentlyActive
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
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                layoutManager?.let {
                    val visibleItemCount = it.childCount
                    val totalItemCount = it.itemCount
                    val pastVisibleItemCount = it.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisibleItemCount) >= totalItemCount && dy > 0) {
                        detectViewModel.updateDetectScroll(true)
                    } else {
                        detectViewModel.updateDetectScroll(false)
                    }
                }
            }
        })
    }

}