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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.core.database.data.entities.orders.OrderWithItems
import com.example.ecommerce.core.ui.state.UiState
import com.example.ecommerce.core.utils.SnackBarCustom
import com.example.ecommerce.core.utils.detectScrollEnd
import com.example.ecommerce.databinding.FragmentOrdersBinding
import com.example.ecommerce.features.orders.presentation.screens.adapter.order_adapter.OrderAdapter
import com.example.ecommerce.features.orders.presentation.viewmodel.IOrderViewModel
import com.example.ecommerce.features.orders.presentation.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private val orderViewModel: IOrderViewModel by viewModels<OrderViewModel>()
    private lateinit var root: View
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel.getOrders()
        detectScrollEnd(binding.ordersRecyclerView)
        orderState()

    }



    private fun orderRecyclerView(orderWithItems: List<OrderWithItems>) {
        println(orderWithItems)
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = OrderAdapter(
            orderWithItems = orderWithItems
        ) {
            val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(
                orderDetailsArgs = it.toTypedArray()

            )
            findNavController().navigate(action)
        }
        binding.ordersRecyclerView.adapter = orderAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}