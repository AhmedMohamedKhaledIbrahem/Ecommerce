package com.example.ecommerce.features.orders.presentation.screens.orders

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter.OrderAdapter
import com.example.ecommerce.features.orders.presentation.viewmodel.IOrderViewModel
import com.example.ecommerce.features.orders.presentation.viewmodel.OrderViewModel
import com.example.ecommerce.features.product.presentation.screen.product.ProductFragmentDirections
import com.example.ecommerce.features.product.presentation.screen.product_details.ProductDetailsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private val orderViewModel: IOrderViewModel by viewModels<OrderViewModel>()
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel.getOrders()
        orderState()

    }

    private fun initView(view: View) {
        root = view
        recyclerView = view.findViewById(R.id.ordersRecyclerView)
    }

    private fun orderRecyclerView(orderWithItems: List<OrderWithItems>) {
        println(orderWithItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter(
            context = requireContext(),
            orderWithItems = orderWithItems
        ) {
            val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                orderDetailsArgs = it.toTypedArray()

            )
            findNavController().navigate(action)
        }
        recyclerView.adapter = orderAdapter
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

    private fun orderLoadingState(state: UiState.Loading) {
        when (state.source) {
            "getOrders" -> {

            }
        }
    }

    private fun orderSuccessState(state: UiState.Success<Any>) {
        when (state.source) {
            "getOrders" -> {
                orderItemCollect()
            }
        }
    }

    private fun orderItemCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.orderItemState.collect { orderWithItems ->
                    orderRecyclerView(orderWithItems)
                }
            }
        }
    }

    private fun orderErrorState(state: UiState.Error) {
        when (state.source) {
            "getOrders" -> {
                SnackBarCustom.showSnackbar(
                    view = root,
                    message = state.message
                )
            }
        }
    }


}